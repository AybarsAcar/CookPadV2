package com.aybarsacar.cookpadversion2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.models.ExtendedIngredient
import com.aybarsacar.cookpadversion2.utils.Constants
import com.aybarsacar.cookpadversion2.utils.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredient_row_layout.view.*
import java.util.*


class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

  private var _ingredients = emptyList<ExtendedIngredient>()

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredient_row_layout, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.itemView.ingredientImageView.load(Constants.BASE_INGREDIENTS_IMAGE_URL + _ingredients[position].image) {
      crossfade(600)
      error(R.drawable.ic_no_photos)
    }


    holder.itemView.ingredientName.text = _ingredients[position].name.replaceFirstChar {
      if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
      ) else it.toString()
    }

    holder.itemView.ingredientAmount.text = _ingredients[position].amount.toString()
    holder.itemView.ingredientUnit.text = _ingredients[position].unit
    holder.itemView.ingredientConsistency.text = _ingredients[position].consistency
    holder.itemView.ingredientOriginal.text = _ingredients[position].original
  }

  override fun getItemCount(): Int {
    return _ingredients.size
  }


  fun setData(ingredients: List<ExtendedIngredient>) {

    val diffUtil = RecipesDiffUtil<ExtendedIngredient>(_ingredients, ingredients)

    val diffUtilResult = DiffUtil.calculateDiff(diffUtil)

    _ingredients = ingredients

    diffUtilResult.dispatchUpdatesTo(this)
  }
}