package com.aybarsacar.cookpadversion2.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aybarsacar.cookpadversion2.adapters.FavouriteRecipesAdapter
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity


class FavouriteRecipesBinding {

  companion object {

    /**
     * first attribute viewVisibility refers to the second parameter of this binding function
     * second attribute setData refers to the third parameter of this binding function
     *
     * it is the method signature of a binding method and annotation
     */
    @BindingAdapter("viewVisibility", "setData", requireAll = false)
    @JvmStatic
    fun setDataAndViewVisibility(
      view: View,
      favouriteRecipeEntities: List<FavouriteRecipeEntity>?,
      adapter: FavouriteRecipesAdapter?
    ) {

      if (favouriteRecipeEntities.isNullOrEmpty()) {

        when (view) {

          is ImageView -> {
            view.visibility = View.VISIBLE
          }

          is TextView -> {
            view.visibility = View.VISIBLE
          }

          is RecyclerView -> {
            view.visibility = View.GONE
          }
        }
      } else {

        when (view) {

          is ImageView -> {
            view.visibility = View.GONE
          }

          is TextView -> {
            view.visibility = View.GONE
          }

          is RecyclerView -> {
            view.visibility = View.VISIBLE
            adapter?.setData(favouriteRecipeEntities)
          }
        }
      }
    }

  }
}