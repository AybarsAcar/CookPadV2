package com.aybarsacar.cookpadversion2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aybarsacar.cookpadversion2.models.Result
import com.aybarsacar.cookpadversion2.utils.Constants


@Entity(tableName = Constants.RECIPES_TABLE)
class RecipesEntity(var result: Result) {

  @PrimaryKey(autoGenerate = false)
  var id: Int = 0

}