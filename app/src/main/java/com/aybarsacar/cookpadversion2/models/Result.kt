package com.aybarsacar.cookpadversion2.models


import com.google.gson.annotations.SerializedName

data class Result(
  @SerializedName("results")
  val recipes: List<Recipe>,
)