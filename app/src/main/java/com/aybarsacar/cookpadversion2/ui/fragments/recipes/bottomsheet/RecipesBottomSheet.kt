package com.aybarsacar.cookpadversion2.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aybarsacar.cookpadversion2.databinding.FragmentRecipesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * will be the bottom sheet of the application
 */
class RecipesBottomSheet : BottomSheetDialogFragment() {

  private var mBinding: FragmentRecipesBottomSheetBinding? = null
  private val _binding get() = mBinding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mBinding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

    return _binding.root
  }


  override fun onDestroy() {
    super.onDestroy()
    mBinding = null
  }

}