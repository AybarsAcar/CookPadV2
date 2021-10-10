package com.aybarsacar.cookpadversion2.data.network

import com.aybarsacar.cookpadversion2.models.FoodJoke
import com.aybarsacar.cookpadversion2.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface RecipesApi {

  @GET("/recipes/complexSearch")
  suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<Result>


  @GET("/recipes/complexSearch")
  suspend fun searchRecipes(@QueryMap searchQuery: Map<String, String>): Response<Result>


  @GET("/food/jokes/random")
  suspend fun getFoodJoke(@Query("apiKey") apiKey: String): Response<FoodJoke>
}