package com.aybarsacar.cookpadversion2.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.aybarsacar.cookpadversion2.data.database.RecipesEntity
import com.aybarsacar.cookpadversion2.models.Result
import com.aybarsacar.cookpadversion2.utils.NetworkResult


/**
 * data binding adapter for the Recipes Fragment
 * this is an alternative dataBinding pattern to viewBinding
 */
class RecipesBinding {

  companion object {

    @BindingAdapter("readApiResponse", "readLocalCache", requireAll = true)
    @JvmStatic
    fun errorImageViewVisibility(
      view: View,
      apiResponse: NetworkResult<Result>?,
      localCache: List<RecipesEntity>?
    ) {

      if (apiResponse is NetworkResult.Error && localCache.isNullOrEmpty()) {
        view.visibility = View.VISIBLE
      } else if (apiResponse is NetworkResult.Loading) {
        view.visibility = View.GONE
      } else if (apiResponse is NetworkResult.Success) {
        view.visibility = View.GONE
      }

    }

  }

}