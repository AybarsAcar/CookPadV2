package com.aybarsacar.cookpadversion2.data.network

import com.aybarsacar.cookpadversion2.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RecipesApi {

  @GET("/recipes/complexSearch")
  suspend fun getRecipes(@QueryMap queries: Map<String, String>): Response<Result>

}