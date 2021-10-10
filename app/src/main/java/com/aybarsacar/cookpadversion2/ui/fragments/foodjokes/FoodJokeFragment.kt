package com.aybarsacar.cookpadversion2.ui.fragments.foodjokes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aybarsacar.cookpadversion2.BuildConfig
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.databinding.FragmentFoodJokeBinding
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

  private var mBinding: FragmentFoodJokeBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!


  private val _mainViewModel by viewModels<MainViewModel>()

  // current joke displayed on the screen
  private var _currentJoke = "No Food Joke"


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mBinding = FragmentFoodJokeBinding.inflate(inflater, container, false)

    setHasOptionsMenu(true)

    _binding.lifecycleOwner = viewLifecycleOwner
    _binding.mainViewModel = _mainViewModel

    _mainViewModel.getFoodJoke(BuildConfig.SPOONACULAR_API_KEY)
    _mainViewModel.foodJokeResponse.observe(viewLifecycleOwner, { response ->
      when (response) {
        is NetworkResult.Success -> {
          _binding.foodJokeTextView.text = response.data?.text

          // cache the current joke
          response.data?.let {
            _currentJoke = it.text
          }
        }

        is NetworkResult.Error -> {
          Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()

          // display the latest joke cached in our local device db
          loadFoodJokeFromCache()
        }

        is NetworkResult.Loading -> {
          Log.d("FoodJokeFragment", "Loading")
        }
      }
    })

    return _binding.root
  }


  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.food_joke_menu, menu)
  }


  /**
   * will be used to implement share funcitonlaity
   */
  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    if (item.itemId == R.id.shareFoodJokeMenu) {
      val shareIntent = Intent().apply {
        this.action = Intent.ACTION_SEND
        this.putExtra(Intent.EXTRA_INTENT, _currentJoke)
        this.type = "text/plain"
      }
      startActivity(shareIntent)
    }

    return super.onOptionsItemSelected(item)
  }


  private fun loadFoodJokeFromCache() {
    lifecycleScope.launch {
      _mainViewModel.readFoodJoke.observe(viewLifecycleOwner, {
        if (!it.isNullOrEmpty()) {
          _binding.foodJokeTextView.text = it[0].foodJoke.text

          // cache the current joke
          _currentJoke = it[0].foodJoke.text
        }
      })
    }
  }


  override fun onDestroy() {
    super.onDestroy()
    mBinding = null
  }
}