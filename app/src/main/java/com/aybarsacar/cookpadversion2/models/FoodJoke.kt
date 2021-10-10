package com.aybarsacar.cookpadversion2.models

import com.google.gson.annotations.SerializedName


data class FoodJoke(
  @SerializedName("text")
  val text: String
)