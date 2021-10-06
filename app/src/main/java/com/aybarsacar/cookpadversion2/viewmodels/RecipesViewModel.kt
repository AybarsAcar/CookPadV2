package com.aybarsacar.cookpadversion2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aybarsacar.cookpadversion2.BuildConfig
import com.aybarsacar.cookpadversion2.data.DataStoreRepository
import com.aybarsacar.cookpadversion2.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipesViewModel @Inject constructor(
  application: Application,
  private val _dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

  private var _mealType = Constants.DEFAULT_MEAL_TYPE
  private var _dietType = Constants.DEFAULT_DIET_TYPE


  val readMealAndDietType = _dataStoreRepository.readMealAndDietType

  fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
    viewModelScope.launch(Dispatchers.IO) {
      _dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }
  }


  fun applyQueries(): HashMap<String, String> {

    val queries = HashMap<String, String>()

    viewModelScope.launch {
      readMealAndDietType.collect { value ->
        _mealType = value.selectedMealType
        _dietType = value.selectedDietType
      }
    }

    queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_COUNT
    queries[Constants.QUERY_API_KEY] = BuildConfig.SPOONACULAR_API_KEY
    queries[Constants.QUERY_TYPE] = _mealType
    queries[Constants.QUERY_DIET] = _dietType
    queries[Constants.QUERY_ADD_RECIPE_INFO] = "true"
    queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

    return queries
  }


}