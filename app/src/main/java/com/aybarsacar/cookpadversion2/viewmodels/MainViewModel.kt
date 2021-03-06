package com.aybarsacar.cookpadversion2.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.aybarsacar.cookpadversion2.data.Repository
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.FoodJokeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.RecipesEntity
import com.aybarsacar.cookpadversion2.models.FoodJoke
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
  var readRecipes: LiveData<List<RecipesEntity>> = _repository.localDataSource.readRecipes().asLiveData()

  val readFavouriteRecipes: LiveData<List<FavouriteRecipeEntity>> =
    _repository.localDataSource.readFavouriteRecipes().asLiveData()

  val readFoodJoke: LiveData<List<FoodJokeEntity>> = _repository.localDataSource.getFoodJoke().asLiveData()


  fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch(Dispatchers.IO) {
    _repository.localDataSource.insertFoodJoke(foodJokeEntity)
  }


  fun insertFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity) =
    viewModelScope.launch(Dispatchers.IO) {
      _repository.localDataSource.insertFavouriteRecipe(favouriteRecipeEntity)
    }


  fun deleteFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity) =
    viewModelScope.launch(Dispatchers.IO) {
      _repository.localDataSource.deleteFavouriteRecipe(favouriteRecipeEntity)
    }


  fun deleteAllFavouriteRecipes() =
    viewModelScope.launch(Dispatchers.IO) {
      _repository.localDataSource.deleteAllFavouriteRecipes()
    }


  /**
   * used to cache the recipes in the local sqlite database
   */
  private fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch(Dispatchers.IO) {
    _repository.localDataSource.insertRecipes(recipesEntity)
  }


  // Retrofit
  var recipesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()
  var searchRecipesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()
  var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()


  /**
   * our coroutine
   */
  fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
    getRecipesSafeCall(queries)
  }


  fun getFoodJoke(apiKey: String) = viewModelScope.launch {
    getFoodJokeSafeCall(apiKey)
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


  private suspend fun getFoodJokeSafeCall(apiKey: String) {

    foodJokeResponse.value = NetworkResult.Loading()

    if (hasInternetConnection()) {

      try {

        val response = _repository.remoteDataSource.getFoodJoke(apiKey)

        // store the response in our live data
        foodJokeResponse.value = handleFoodJokeResponse(response)

        val foodJoke = foodJokeResponse.value!!.data

        foodJoke?.let {
          offlineCacheFoodJoke(it)
        }

      } catch (e: Exception) {

        foodJokeResponse.value = NetworkResult.Error("Recipes not found")

      }

    } else {
      foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
    }

  }


  /**
   * Caches the result we get from the API
   */
  private fun offlineCacheRecipes(foodRecipe: Result) {

    val recipesEntity = RecipesEntity(foodRecipe)

    insertRecipes(recipesEntity)
  }


  /**
   * Caches the result we get from the API
   */
  private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {

    val foodJokeEntity = FoodJokeEntity(foodJoke)

    insertFoodJoke(foodJokeEntity)
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


  private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
    when {
      // these are the messages we get from the API
      response.message().toString().contains("timeout") -> {
        return NetworkResult.Error("Timeout")
      }

      response.code() == 402 -> {
        return NetworkResult.Error("API Key Limited")
      }

      response.isSuccessful -> {
        val foodJoke = response.body()
        return NetworkResult.Success(foodJoke!!)
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