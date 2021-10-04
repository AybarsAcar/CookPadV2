package com.aybarsacar.cookpadversion2.utils


class Constants {

  companion object {

    const val BASE_URL = "https://api.spoonacular.com"

    // API Query Keys
    const val QUERY_NUMBER = "number"
    const val QUERY_API_KEY = "apiKey"
    const val QUERY_TYPE = "type"
    const val QUERY_DIET = "diet"
    const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
    const val QUERY_FILL_INGREDIENTS = "fillIngredients"

    // Room Database
    const val ROOM_DATABASE_NAME = "recipes_database"
    const val RECIPES_TABLE = "recipes_table"
  }

}