package com.aybarsacar.cookpadversion2.ui.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aybarsacar.cookpadversion2.adapters.RecipesAdapter
import com.aybarsacar.cookpadversion2.databinding.FragmentRecipesBinding
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.aybarsacar.cookpadversion2.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint


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

    // show the shimmer
    _binding.shimmerRecyclerView.showShimmer()

    setupRecyclerView()
    requestApiData()

    return _binding.root
  }


  override fun onDestroyView() {
    super.onDestroyView()
    mBinding = null
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
          Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
        }

        is NetworkResult.Loading -> {
          displayShimmerEffect()
        }
      }

    })
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