package com.aybarsacar.cookpadversion2.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aybarsacar.cookpadversion2.adapters.IngredientsAdapter
import com.aybarsacar.cookpadversion2.databinding.FragmentIngredientsBinding
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.utils.Constants
import kotlinx.android.synthetic.main.fragment_ingredients.view.*


class IngredientsFragment : Fragment() {

  private var mBinding: FragmentIngredientsBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!

  private val _adapter: IngredientsAdapter by lazy { IngredientsAdapter() }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mBinding = FragmentIngredientsBinding.inflate(inflater, container, false)

    val args = arguments
    val currentRecipe: Recipe? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

    setupRecyclerView(_binding.root)

    currentRecipe?.let {
      _adapter.setData(it.extendedIngredients)
    }

    // Inflate the layout for this fragment
    return _binding.root
  }


  private fun setupRecyclerView(view: View) {
    view.ingredientsRecyclerView.adapter = _adapter
    view.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
  }
}