package com.aybarsacar.cookpadversion2.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aybarsacar.cookpadversion2.models.FoodJoke
import com.aybarsacar.cookpadversion2.utils.Constants


@Entity(tableName = Constants.FOOD_JOKE_TABLE)
class FoodJokeEntity(@Embedded var foodJoke: FoodJoke) {

  @PrimaryKey(autoGenerate = false)
  var id: Int = 0
}