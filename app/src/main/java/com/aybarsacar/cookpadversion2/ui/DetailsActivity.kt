package com.aybarsacar.cookpadversion2.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.databinding.ActivityDetailsBinding
import com.aybarsacar.cookpadversion2.ui.fragments.PagerAdapter
import com.aybarsacar.cookpadversion2.ui.fragments.ingredients.IngredientsFragment
import com.aybarsacar.cookpadversion2.ui.fragments.instructions.InstructionsFragment
import com.aybarsacar.cookpadversion2.ui.fragments.overview.OverviewFragment

class DetailsActivity : AppCompatActivity() {

  private lateinit var _binding: ActivityDetailsBinding

  private val args by navArgs<DetailsActivityArgs>()


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
    resultBundle.putParcelable("recipeBundle", args.recipe)

    val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)
    _binding.viewPager.adapter = adapter
    _binding.tabLayout.setupWithViewPager(_binding.viewPager)
  }


  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    if (item.itemId == android.R.id.home) {
      finish()
    }

    return super.onOptionsItemSelected(item)
  }
}