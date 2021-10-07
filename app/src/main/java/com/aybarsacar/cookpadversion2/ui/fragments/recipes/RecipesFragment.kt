package com.aybarsacar.cookpadversion2.ui.fragments.recipes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.adapters.RecipesAdapter
import com.aybarsacar.cookpadversion2.databinding.FragmentRecipesBinding
import com.aybarsacar.cookpadversion2.utils.NetworkListener
import com.aybarsacar.cookpadversion2.utils.NetworkResult
import com.aybarsacar.cookpadversion2.utils.observeOnce
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.aybarsacar.cookpadversion2.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

  private var mBinding: FragmentRecipesBinding? = null

  private val _adapter by lazy { RecipesAdapter() }

  private lateinit var _mainViewModel: MainViewModel
  private lateinit var _recipesViewModel: RecipesViewModel

  private lateinit var _networkListener: NetworkListener

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!

  private val _args by navArgs<RecipesFragmentArgs>()


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

    _binding.mainViewModel = _mainViewModel


    _binding.recipeFilterFab.setOnClickListener {

      if (_recipesViewModel.networkStatus) {
        // only display the bottom sheet if internet is available
        findNavController().navigate(RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheet())
      } else {
        // otherwise toast no internet connection
        _recipesViewModel.showNetworkStatus()
      }
    }

    // show the shimmer
    _binding.shimmerRecyclerView.showShimmer()

    setHasOptionsMenu(true)

    setupRecyclerView()

    _recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
      _recipesViewModel.backOnline = it
    })

    lifecycleScope.launch {
      _networkListener = NetworkListener()
      _networkListener.checkNetworkAvailability(requireContext()).collect {
        _recipesViewModel.networkStatus = it
        _recipesViewModel.showNetworkStatus()

        readDatabase()
      }
    }

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

        // if the local storage is not empty and we are not coming back from the bottom sheet
        // we will use the data from the local device's Sqlite database
        if (it.isNotEmpty() && !_args.backFromBottomSheet) {
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


  private fun searchApiData(searchQuery: String) {
    displayShimmerEffect()

    _mainViewModel.searchRecipes(_recipesViewModel.applySearchQueries(searchQuery))

    _mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner, { response ->

      when (response) {
        is NetworkResult.Success -> {
          hideShimmerEffect()

          val result = response.data
          result?.let {
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


  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.recipes_menu, menu)

    val search = menu.findItem(R.id.menu_search)
    val searchView = search.actionView as? SearchView

    searchView?.isSubmitButtonEnabled = true
    searchView?.setOnQueryTextListener(this)
  }


  /**
   * called whenever we hit submit to search
   */
  override fun onQueryTextSubmit(query: String?): Boolean {

    if (query != null) {
      // search for the API data
      searchApiData(query)
    }

    return true
  }


  /**
   * called whenever the text is changed in the search widget
   */
  override fun onQueryTextChange(p0: String?): Boolean {
    return true
  }


  private fun displayShimmerEffect() {
    _binding.shimmerRecyclerView.showShimmer()
  }

  private fun hideShimmerEffect() {
    _binding.shimmerRecyclerView.hideShimmer()
  }
}