package com.aybarsacar.cookpadversion2.data.database

import androidx.room.TypeConverter
import com.aybarsacar.cookpadversion2.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * to convert Kotlin objects to json data
 */
class RecipesTypeConverter {

  var _gson = Gson()


  @TypeConverter
  fun recipeToString(result: Result): String {
    return _gson.toJson(result)
  }


  @TypeConverter
  fun stringToRecipe(data: String): Result {

    val listType = object : TypeToken<Result>() {}.type

    return _gson.fromJson(data, listType)
  }

}