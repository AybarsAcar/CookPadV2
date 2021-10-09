package com.aybarsacar.cookpadversion2.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.databinding.FavouriteRecipeRowLayoutBinding
import com.aybarsacar.cookpadversion2.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.aybarsacar.cookpadversion2.utils.RecipesDiffUtil
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourite_recipe_row_layout.view.*


class FavouriteRecipesAdapter(
  private val _requireActivity: FragmentActivity,
  private val _mainViewModel: MainViewModel
) :
  RecyclerView.Adapter<FavouriteRecipesAdapter.ViewHolder>(), ActionMode.Callback {

  private lateinit var _actionMode: ActionMode
  private lateinit var _rootView: View

  private var _multiSelection = false
  private var _selectedRecipes = arrayListOf<FavouriteRecipeEntity>()
  private var _myViewHolders = arrayListOf<ViewHolder>()

  private var _favouriteRecipes = emptyList<FavouriteRecipeEntity>()

  class ViewHolder(private val _binding: FavouriteRecipeRowLayoutBinding) : RecyclerView.ViewHolder(_binding.root) {

    fun bind(favouriteRecipeEntity: FavouriteRecipeEntity) {
      _binding.favouriteRecipeEntity = favouriteRecipeEntity
      _binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FavouriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    _myViewHolders.add(holder)

    _rootView = holder.itemView.rootView

    val currentRecipe = _favouriteRecipes[position]

    holder.bind(currentRecipe)

    // single click listener
    holder.itemView.favouriteRecipesRowLayout.setOnClickListener {

      if (_multiSelection) {
        applySelection(holder, currentRecipe)
      } else {

        val action =
          FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(currentRecipe.recipe)

        holder.itemView.findNavController().navigate(action)
      }
    }


    holder.itemView.favouriteRecipesRowLayout.setOnLongClickListener {

      if (!_multiSelection) {

        _multiSelection = true

        _requireActivity.startActionMode(this)

        applySelection(holder, currentRecipe)
        true

      } else {
        _multiSelection = false

        false
      }

    }
  }

  override fun getItemCount(): Int {
    return _favouriteRecipes.size
  }


  override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {

    actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)

    _actionMode = actionMode!!

    // change the colour of the status bar
    applyStatusBarColour(R.color.contextualStatusBarColor)

    return true
  }

  override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
    return true
  }

  override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {

    if (menu?.itemId == R.id.deleteFavouriteRecipeMenu) {
      _selectedRecipes.forEach {
        _mainViewModel.deleteFavouriteRecipe(it)
      }

      displaySnackBar("${_selectedRecipes.size} recipes removed")

      _multiSelection = false
      _selectedRecipes.clear()
      _actionMode.finish()
    }

    return true
  }

  override fun onDestroyActionMode(actionMode: ActionMode?) {

    _myViewHolders.forEach {
      changeRecipeStyle(it, R.color.cardBackgroundColor, R.color.strokeColor)
    }

    _multiSelection = false
    _selectedRecipes.clear()

    // set the colour back to default colour
    applyStatusBarColour(R.color.statusBarColor)
  }


  private fun applySelection(holder: ViewHolder, currentRecipe: FavouriteRecipeEntity) {

    if (_selectedRecipes.contains(currentRecipe)) {
      _selectedRecipes.remove(currentRecipe)

      // set back to the default colours
      changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)

      applyActionModeTitle()

    } else {
      _selectedRecipes.add(currentRecipe)

      // change the colours to give the selection loop
      changeRecipeStyle(holder, R.color.cardBackgroundSelectedColor, R.color.colorPrimary)

      applyActionModeTitle()
    }
  }


  private fun changeRecipeStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {

    holder.itemView.favouriteRecipesRowLayout
      .setBackgroundColor(ContextCompat.getColor(_requireActivity, backgroundColor))

    holder.itemView.favouriteRowCardView.strokeColor = ContextCompat.getColor(_requireActivity, strokeColor)
  }


  private fun applyStatusBarColour(colour: Int) {
    _requireActivity.window.statusBarColor = ContextCompat.getColor(_requireActivity, colour)
  }


  private fun applyActionModeTitle() {
    when (_selectedRecipes.size) {
      0 -> _actionMode.finish() // close the action mode

      1 -> _actionMode.title = "${_selectedRecipes.size} item selected"

      else -> _actionMode.title = "${_selectedRecipes.size} items selected"
    }
  }


  private fun displaySnackBar(message: String) {
    Snackbar.make(_rootView, message, Snackbar.LENGTH_LONG).setAction("Ok") { }.show()
  }


  fun clearContextualMode() {

    if (this::_actionMode.isInitialized) {
      _actionMode.finish()
    }
  }


  fun setData(favouriteRecipes: List<FavouriteRecipeEntity>) {

    val diffUtil = RecipesDiffUtil(_favouriteRecipes, favouriteRecipes)

    val diffUtilResult = DiffUtil.calculateDiff(diffUtil)

    _favouriteRecipes = favouriteRecipes

    diffUtilResult.dispatchUpdatesTo(this)
  }
}