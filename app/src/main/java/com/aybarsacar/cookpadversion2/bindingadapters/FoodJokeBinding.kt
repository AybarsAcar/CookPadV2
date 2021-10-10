package com.aybarsacar.cookpadversion2.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aybarsacar.cookpadversion2.data.database.entities.FoodJokeEntity
import com.aybarsacar.cookpadversion2.models.FoodJoke
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import com.google.android.material.card.MaterialCardView


class FoodJokeBinding {

  companion object {

    @BindingAdapter("readFoodJokeApiResponse", "readFoodJokeDatabase", requireAll = false)
    @JvmStatic
    fun setCardAndProgressVisibility(
      view: View,
      apiResponse: NetworkResult<FoodJoke>?,
      database: List<FoodJokeEntity>?
    ) {

      when (apiResponse) {

        is NetworkResult.Loading -> {
          when (view) {
            is ProgressBar -> view.visibility = View.VISIBLE
            is MaterialCardView -> view.visibility = View.GONE
          }
        }

        is NetworkResult.Error -> {
          when (view) {
            is ProgressBar -> view.visibility = View.GONE
            is MaterialCardView -> {
              view.visibility = View.VISIBLE
              if (database != null) {
                if (database.isEmpty()) {
                  view.visibility = View.GONE
                }
              }
            }
          }
        }

        is NetworkResult.Success -> {
          when (view) {
            is ProgressBar -> view.visibility = View.GONE
            is MaterialCardView -> view.visibility = View.VISIBLE
          }
        }

      }

    }


    /**
     * called when there is no network connection and local device db is empty
     * or when there is an api error response and local device db is empty
     */
    @BindingAdapter("readFoodJokeApiResponseConnectionCheck", "readFoodJokeDatabaseConnectionCheck", requireAll = true)
    @JvmStatic
    fun setErrorViewsVisibility(
      view: View,
      apiResponse: NetworkResult<FoodJoke>?,
      database: List<FoodJokeEntity>?
    ) {

      database?.let {
        if (it.isEmpty()) {
          view.visibility = View.VISIBLE

          if (view is TextView) {
            if (apiResponse != null) {
              view.text = apiResponse.message.toString()
            }
          }
        }
      }

      if (apiResponse is NetworkResult.Success || apiResponse is NetworkResult.Loading) {
        view.visibility = View.GONE
      }

    }

  }

}