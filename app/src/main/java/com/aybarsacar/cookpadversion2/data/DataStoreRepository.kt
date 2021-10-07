package com.aybarsacar.cookpadversion2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.aybarsacar.cookpadversion2.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(Constants.PREFERENCES_NAME)

/**
 * Used to store our chips in Data Store Preferences
 * The Access Repository layer to the Data Store Preferences
 */
@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val _context: Context) {

  private object PreferenceKeys {
    val selectedMealType = stringPreferencesKey(Constants.PREFERENCES_MEAL_TYPE)
    val selectedMealTypeId = intPreferencesKey(Constants.PREFERENCES_MEAL_TYPE_ID)

    val selectedDietType = stringPreferencesKey(Constants.PREFERENCES_DIET_TYPE)
    val selectedDietTypeId = intPreferencesKey(Constants.PREFERENCES_DIET_TYPE_ID)

    val backOnline = booleanPreferencesKey(Constants.PREFERENCES_BACK_ONLINE)
  }


  private val _dataStore: DataStore<Preferences> = _context.dataStore


  suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {

    _dataStore.edit { preferences ->
      preferences[PreferenceKeys.selectedMealType] = mealType
      preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
      preferences[PreferenceKeys.selectedDietType] = dietType
      preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
    }

  }


  suspend fun saveBackOnline(backOnline: Boolean) {
    _dataStore.edit { preferences ->
      preferences[PreferenceKeys.backOnline] = backOnline
    }
  }


  val readMealAndDietType: Flow<MealAndDietType> = _dataStore.data.catch { e ->
    if (e is IOException) {
      emit(emptyPreferences())
    } else {
      throw e
    }
  }
    .map { preferences ->
      val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: Constants.DEFAULT_MEAL_TYPE
      val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
      val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: Constants.DEFAULT_DIET_TYPE
      val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

      MealAndDietType(selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeId)
    }


  val readBackOnline: Flow<Boolean> = _dataStore.data.catch { e ->
    if (e is IOException) {
      emit(emptyPreferences())
    } else {
      throw e
    }
  }
    .map { preferences ->
      val backOnline = preferences[PreferenceKeys.backOnline] ?: false
      backOnline
    }

}


data class MealAndDietType(
  val selectedMealType: String,
  val selectedMealTypeId: Int,
  val selectedDietType: String,
  val selectedDietTypeId: Int
)