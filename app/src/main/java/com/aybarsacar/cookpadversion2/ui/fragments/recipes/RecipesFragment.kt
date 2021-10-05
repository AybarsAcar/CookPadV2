package com.aybarsacar.cookpadversion2.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aybarsacar.cookpadversion2.adapters.RecipesAdapter
import com.aybarsacar.cookpadversion2.databinding.FragmentRecipesBinding
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import com.aybarsacar.cookpadversion2.utils.observeOnce
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.aybarsacar.cookpadversion2.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment() {

  private var mBinding: FragmentRecipesBinding? = null

  private val _adapter by lazy { RecipesAdapter() }

  private lateinit var _mainViewModel: MainViewModel
  private lateinit var _recipesViewModel: RecipesViewModel

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // initialise the view models
    _mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    _recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
  }


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mBinding = FragmentRecipesBinding.inflate(inflater, container, false)

//    _binding.lifecycleOwner = this
    _binding.mainViewModel = _mainViewModel


    _binding.recipeFilterFab.setOnClickListener {
      findNavController().navigate(RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheet())
    }

    // show the shimmer
    _binding.shimmerRecyclerView.showShimmer()

    setupRecyclerView()

    readDatabase()

    return _binding.root
  }


  override fun onDestroyView() {
    super.onDestroyView()
    mBinding = null
  }


  /**
   * reads the data in the local sqlite database of the device
   */
  private fun readDatabase() {

    lifecycleScope.launch {

      _mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, {
        if (it.isNotEmpty()) {
          _adapter.setData(it[0].result)
          hideShimmerEffect()
        } else {

          // otherwise, request the data from the API
          requestApiData()
        }
      })

    }
  }


  private fun requestApiData() {
    _mainViewModel.getRecipes(_recipesViewModel.applyQueries())

    _mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->

      when (response) {
        is NetworkResult.Success -> {
          hideShimmerEffect()
          response.data?.let {
            _adapter.setData(it)
          }
        }

        is NetworkResult.Error -> {
          hideShimmerEffect()

          // load the previous data from the cache
          loadDataFromCache()

          Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
        }

        is NetworkResult.Loading -> {
          displayShimmerEffect()
        }
      }

    })
  }


  /**
   * only loads the data from cache
   * used when there is no internet connection available on the device
   */
  private fun loadDataFromCache() {

    lifecycleScope.launch {

      _mainViewModel.readRecipes.observe(viewLifecycleOwner, {
        if (it.isEmpty()) {
          _adapter.setData(it[0].result)
        }
      })

    }
  }


  private fun setupRecyclerView() {
    _binding.shimmerRecyclerView.adapter = _adapter
    _binding.shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    displayShimmerEffect()
  }


  private fun displayShimmerEffect() {
    _binding.shimmerRecyclerView.showShimmer()
  }

  private fun hideShimmerEffect() {
    _binding.shimmerRecyclerView.hideShimmer()
  }
}