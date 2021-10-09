package com.aybarsacar.cookpadversion2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.RecipesEntity


@Database(entities = [RecipesEntity::class, FavouriteRecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

  abstract fun recipesDao(): RecipesDao

}