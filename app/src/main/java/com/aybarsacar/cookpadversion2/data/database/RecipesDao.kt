package com.aybarsacar.cookpadversion2.data.database

import androidx.room.*
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRecipes(recipesEntity: RecipesEntity)


  @Query("select * from recipes_table order by id asc")
  fun getRecipes(): Flow<List<RecipesEntity>>


  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity)


  @Query("select * from favourite_recipes_table order by id asc")
  fun getFavouriteRecipes(): Flow<List<FavouriteRecipeEntity>>


  @Delete
  suspend fun deleteFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity)


  @Query("delete from favourite_recipes_table")
  suspend fun deleteAllFavouriteRecipes()
}