package com.aybarsacar.cookpadversion2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecipes(recipesEntity: RecipesEntity)


  @Query("select * from recipes_table order by id asc")
  fun getRecipes(): Flow<List<RecipesEntity>>

}