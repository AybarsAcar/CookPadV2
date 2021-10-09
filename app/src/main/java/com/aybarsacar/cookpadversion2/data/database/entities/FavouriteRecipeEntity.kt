package com.aybarsacar.cookpadversion2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.utils.Constants


@Entity(tableName = Constants.FAVOURITE_RECIPES_TABLE)
data class FavouriteRecipeEntity(
  @PrimaryKey(autoGenerate = true) var id: Int,
  var recipe: Recipe
)
