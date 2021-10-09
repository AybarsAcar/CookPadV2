package com.aybarsacar.cookpadversion2.data.database

import androidx.room.TypeConverter
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * to convert Kotlin objects to json data
 * we are converting the objects we receive from the API to json to store in the local sqlite database
 */
class RecipesTypeConverter {

  private val _gson = Gson()


  @TypeConverter
  fun resultToString(result: Result): String {
    return _gson.toJson(result)
  }


  @TypeConverter
  fun stringToResult(data: String): Result {

    val listType = object : TypeToken<Result>() {}.type

    return _gson.fromJson(data, listType)
  }


  @TypeConverter
  fun recipeToString(recipe: Recipe): String {
    return _gson.toJson(recipe)
  }


  @TypeConverter
  fun stringToRecipe(data: String): Recipe {

    val listType = object : TypeToken<Recipe>() {}.type

    return _gson.fromJson(data, listType)
  }

}