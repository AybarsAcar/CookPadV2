package com.aybarsacar.cookpadversion2.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.aybarsacar.cookpadversion2.data.Repository
import com.aybarsacar.cookpadversion2.data.database.RecipesEntity
import com.aybarsacar.cookpadversion2.models.Result
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
  private val _repository: Repository,
  application: Application
) : AndroidViewModel(application) {

  // Room Database
  var readRecipes: LiveData<List<RecipesEntity>> = _repository.localDataSource.readDatabase().asLiveData()

  /**
   * used to cache the recipes in the local sqlite database
   */
  private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
    _repository.localDataSource.insertRecipes(recipesEntity)
  }


  // Retrofit
  var recipesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()
  var searchRecipesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()


  /**
   * our coroutine
   */
  fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
    getRecipesSafeCall(queries)
  }


  fun searchRecipes(searchQueries: Map<String, String>) = viewModelScope.launch {
    searchRecipesSafeCall(searchQueries)
  }


  private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {

    searchRecipesResponse.value = NetworkResult.Loading()

    if (hasInternetConnection()) {

      try {

        val response = _repository.remoteDataSource.searchRecipes(searchQueries)

        // store the response in our live data
        searchRecipesResponse.value = handleFoodRecipesResponse(response)

      } catch (e: Exception) {

        searchRecipesResponse.value = NetworkResult.Error("Recipes not found")

      }

    } else {
      searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
    }

  }


  private suspend fun getRecipesSafeCall(queries: Map<String, String>) {

    recipesResponse.value = NetworkResult.Loading()

    if (hasInternetConnection()) {

      try {

        val response = _repository.remoteDataSource.getRecipes(queries)

        // store the response in our live data
        recipesResponse.value = handleFoodRecipesResponse(response)

        val foodRecipe = recipesResponse.value!!.data
        if (foodRecipe != null) {

          // cache the data
          offlineCacheRecipes(foodRecipe)
        }

      } catch (e: Exception) {

        recipesResponse.value = NetworkResult.Error("Recipes not found")

      }

    } else {
      recipesResponse.value = NetworkResult.Error("No Internet Connection")
    }
  }


  /**
   * Caches the result we get from the API
   */
  private fun offlineCacheRecipes(foodRecipe: Result) {

    val recipesEntity = RecipesEntity(foodRecipe)

    insertRecipes(recipesEntity)

  }


  private fun handleFoodRecipesResponse(response: Response<Result>): NetworkResult<Result>? {
    when {
      // these are the messages we get from the API
      response.message().toString().contains("timeout") -> {
        return NetworkResult.Error("Timeout")
      }

      response.code() == 402 -> {
        return NetworkResult.Error("API Key Limited")
      }

      response.body()!!.recipes.isNullOrEmpty() -> {
        return NetworkResult.Error("Recipes not found")
      }

      response.isSuccessful -> {
        val recipes = response.body()
        return NetworkResult.Success(recipes!!)
      }

      else -> return NetworkResult.Error(response.message())
    }
  }


  /**
   * checks for an internet connection
   */
  private fun hasInternetConnection(): Boolean {

    val connectivityManager =
      getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false

    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return when {
      capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
      capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
      capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
      else -> false
    }
  }

}