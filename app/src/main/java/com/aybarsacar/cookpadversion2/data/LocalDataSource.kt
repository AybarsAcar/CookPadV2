package com.aybarsacar.cookpadversion2.data

import com.aybarsacar.cookpadversion2.data.database.RecipesDao
import com.aybarsacar.cookpadversion2.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(private val _recipesDao: RecipesDao) {

  suspend fun insertRecipes(recipesEntity: RecipesEntity) {
    _recipesDao.insertRecipes(recipesEntity)
  }


  fun readDatabase(): Flow<List<RecipesEntity>> {
    return _recipesDao.getRecipes()
  }

}