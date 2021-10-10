package com.aybarsacar.cookpadversion2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var _binding: ActivityMainBinding
  private lateinit var _navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _binding = ActivityMainBinding.inflate(layoutInflater)

    // set the theme back to normal theme
    setTheme(R.style.AppTheme)

    setContentView(_binding.root)

    val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)

    if (navHostFragment != null) {
      _navController = navHostFragment.findNavController()
      val appBarConfiguration = AppBarConfiguration(
        setOf(R.id.recipesFragment, R.id.favouriteRecipesFragment, R.id.foodJokeFragment)
      )

      _binding.bottomNavigation.setupWithNavController(_navController)
      setupActionBarWithNavController(_navController, appBarConfiguration)
    }
  }


  override fun onSupportNavigateUp(): Boolean {
    return _navController.navigateUp() || super.onSupportNavigateUp()
  }
}