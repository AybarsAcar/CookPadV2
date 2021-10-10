package com.aybarsacar.cookpadversion2.data

import com.aybarsacar.cookpadversion2.data.network.RecipesApi
import com.aybarsacar.cookpadversion2.models.FoodJoke
import com.aybarsacar.cookpadversion2.models.Result
import retrofit2.Response
import javax.inject.Inject


/**
 * request the data from the API
 * Food RecipesApi will be injected into this
 */
class RemoteDataSource @Inject constructor(
  private val _recipeApi: RecipesApi
) {

  /**
   * get recipes with multiple passed in requests
   */
  suspend fun getRecipes(queries: Map<String, String>): Response<Result> {

    return _recipeApi.getRecipes(queries)

  }


  suspend fun searchRecipes(searchQueries: Map<String, String>): Response<Result> {

    return _recipeApi.searchRecipes(searchQueries)

  }


  suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {

    return _recipeApi.getFoodJoke(apiKey)

  }

}