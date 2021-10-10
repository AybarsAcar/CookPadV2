package com.aybarsacar.cookpadversion2.data

import com.aybarsacar.cookpadversion2.data.database.RecipesDao
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.FoodJokeEntity
import com.aybarsacar.cookpadversion2.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val _recipesDao: RecipesDao) {

  suspend fun insertRecipes(recipesEntity: RecipesEntity) {
    _recipesDao.insertRecipes(recipesEntity)
  }


  fun readRecipes(): Flow<List<RecipesEntity>> {
    return _recipesDao.getRecipes()
  }


  fun readFavouriteRecipes(): Flow<List<FavouriteRecipeEntity>> {
    return _recipesDao.getFavouriteRecipes()
  }


  suspend fun insertFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity) {
    _recipesDao.insertFavouriteRecipe(favouriteRecipeEntity)
  }


  suspend fun deleteFavouriteRecipe(favouriteRecipeEntity: FavouriteRecipeEntity) {
    _recipesDao.deleteFavouriteRecipe(favouriteRecipeEntity)
  }


  suspend fun deleteAllFavouriteRecipes() {
    _recipesDao.deleteAllFavouriteRecipes()
  }


  fun getFoodJoke(): Flow<List<FoodJokeEntity>> {
    return _recipesDao.getFoodJoke()
  }


  suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
    _recipesDao.insertFoodJoke(foodJokeEntity)
  }
}