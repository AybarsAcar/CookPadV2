package com.aybarsacar.cookpadversion2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aybarsacar.cookpadversion2.databinding.RecipesRowLayoutBinding
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.models.Result
import com.aybarsacar.cookpadversion2.utils.RecipesDiffUtil


class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

  private var _recipes = emptyList<Recipe>()

  class ViewHolder(private val _binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
      }
    }

    fun bind(recipe: Recipe) {
      _binding.recipe = recipe

      // update layout when there is a change in our data
      // it must be run on the ui thread
      _binding.executePendingBindings()
    }

  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val currentRecipe = _recipes[position]

    holder.bind(currentRecipe)
  }

  override fun getItemCount(): Int {
    return _recipes.size
  }

  /**
   * to set the data of our recipe
   */
  fun setData(data: Result) {

    val recipesDiffUtil = RecipesDiffUtil(_recipes, data.recipes)
    val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)

    _recipes = data.recipes

    // tell recycler view to update the data
    diffUtilResult.dispatchUpdatesTo(this)
  }
}