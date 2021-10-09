package com.aybarsacar.cookpadversion2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.adapters.PagerAdapter
import com.aybarsacar.cookpadversion2.data.database.entities.FavouriteRecipeEntity
import com.aybarsacar.cookpadversion2.databinding.ActivityDetailsBinding
import com.aybarsacar.cookpadversion2.ui.fragments.ingredients.IngredientsFragment
import com.aybarsacar.cookpadversion2.ui.fragments.instructions.InstructionsFragment
import com.aybarsacar.cookpadversion2.ui.fragments.overview.OverviewFragment
import com.aybarsacar.cookpadversion2.utils.Constants
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

  private lateinit var _binding: ActivityDetailsBinding

  private val args by navArgs<DetailsActivityArgs>()

  private val _mainViewModel: MainViewModel by viewModels()

  private var _isRecipeSaved = false
  private var _savedRecipeId = 0


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityDetailsBinding.inflate(layoutInflater)
    setContentView(_binding.root)

    setSupportActionBar(_binding.toolbar)
    _binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    // initialise our Pager Adapter
    // array indexes matter - match your titles
    val fragments = arrayListOf<Fragment>(OverviewFragment(), IngredientsFragment(), InstructionsFragment())
    val titles = arrayListOf<String>("Overview", "Ingredients", "Instructions")

    val resultBundle = Bundle()
    resultBundle.putParcelable(Constants.RECIPE_RESULT_KEY, args.recipe)

    val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)
    _binding.viewPager.adapter = adapter
    _binding.tabLayout.setupWithViewPager(_binding.viewPager)
  }


  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.details_menu, menu)

    val menuItem = menu?.findItem(R.id.saveToFavouritesMenu)

    checkSavedRecipes(menuItem!!)

    return true
  }


  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    if (item.itemId == android.R.id.home) {
      finish()
    } else if (item.itemId == R.id.saveToFavouritesMenu && !_isRecipeSaved) {
      // save the recipe to our favourites table
      saveToFavourites(item)
    } else if (item.itemId == R.id.saveToFavouritesMenu && _isRecipeSaved) {
      // save the recipe to our favourites table
      removeFromFavourites(item)
    }

    return super.onOptionsItemSelected(item)
  }


  private fun checkSavedRecipes(item: MenuItem) {
    _mainViewModel.readFavouriteRecipes.observe(this, { favouriteRecipeEntities ->
      try {
        for (savedRecipe in favouriteRecipeEntities) {

          if (savedRecipe.recipe.id == args.recipe.id) {
            changeMenuItemColour(item, R.color.yellow)

            _savedRecipeId = savedRecipe.id
            _isRecipeSaved = true
          } else {
            changeMenuItemColour(item, R.color.white)
          }

        }
      } catch (e: Exception) {
        Log.e("DetailsActivity", e.message.toString())
      }
    })
  }


  private fun saveToFavourites(item: MenuItem) {

    // create favourites entity
    // id passed in 0 so our table automatically generates one
    val favouriteRecipeToAdd = FavouriteRecipeEntity(0, args.recipe)

    // save to the database through the view model
    _mainViewModel.insertFavouriteRecipe(favouriteRecipeToAdd)

    // update the star icon's colour
    changeMenuItemColour(item, R.color.yellow)

    // snack bar feedback to user
    displaySnackBar("Recipe added to your favourites!")

    _isRecipeSaved = true
  }


  private fun removeFromFavourites(item: MenuItem) {

    // create favourites entity
    // id passed in 0 so our table automatically generates one
    val favouriteRecipeToDelete = FavouriteRecipeEntity(_savedRecipeId, args.recipe)

    _mainViewModel.deleteFavouriteRecipe(favouriteRecipeToDelete)
    changeMenuItemColour(item, R.color.white)

    displaySnackBar("Recipe removed to your favourites")

    _isRecipeSaved = false
  }


  @SuppressLint("ShowToast")
  private fun displaySnackBar(message: String) {
    Snackbar.make(detailsLayout, message, Snackbar.LENGTH_LONG)
      .setAction("Okay") {}
      .show()
  }


  private fun changeMenuItemColour(item: MenuItem, colour: Int) {
    item.icon.setTint(ContextCompat.getColor(this, colour))
  }
}