package com.aybarsacar.cookpadversion2.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.adapters.FavouriteRecipesAdapter
import com.aybarsacar.cookpadversion2.databinding.FragmentFavouriteRecipesBinding
import com.aybarsacar.cookpadversion2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

  private var mBinding: FragmentFavouriteRecipesBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!

  private val _mainViewModel: MainViewModel by viewModels()

  private val _adapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity(), _mainViewModel) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mBinding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false)

    _binding.lifecycleOwner = this
    _binding.mainViewModel = _mainViewModel
    _binding.mAdapter = _adapter

    setHasOptionsMenu(true)

    setupRecyclerView(_binding.favouriteRecipesRecyclerView)

    return _binding.root
  }


  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.favourite_recipes_menu, menu)
  }


  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.deleteAllFavouriteRecipesMenu) {
      _mainViewModel.deleteAllFavouriteRecipes()

      displaySnackBar("All recipes removed")
    }

    return super.onOptionsItemSelected(item)
  }


  private fun displaySnackBar(message: String) {
    Snackbar.make(_binding.root, message, Snackbar.LENGTH_LONG).setAction("Ok") {}.show()
  }


  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = _adapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
  }


  override fun onDestroy() {
    super.onDestroy()
    mBinding = null
    _adapter.clearContextualMode()
  }
}