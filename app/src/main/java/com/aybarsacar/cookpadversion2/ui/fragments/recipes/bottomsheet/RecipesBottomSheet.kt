package com.aybarsacar.cookpadversion2.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.aybarsacar.cookpadversion2.databinding.FragmentRecipesBottomSheetBinding
import com.aybarsacar.cookpadversion2.utils.Constants
import com.aybarsacar.cookpadversion2.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


/**
 * will be the bottom sheet of the application
 */
class RecipesBottomSheet : BottomSheetDialogFragment() {

  private var mBinding: FragmentRecipesBottomSheetBinding? = null
  private val _binding get() = mBinding!!

  private lateinit var _recipesViewModel: RecipesViewModel

  private var _mealTypeChip = Constants.DEFAULT_MEAL_TYPE
  private var _mealTypeChipId = 0
  private var _dietTypeChip = Constants.DEFAULT_DIET_TYPE
  private var _dietTypeChipId = 0


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    _recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
  }


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mBinding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

    // read the data from teh Data Store Preferences through the Recipe View Model as Live Data
    _recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, {
      _mealTypeChip = it.selectedMealType
      _dietTypeChip = it.selectedDietType

      updateChip(it.selectedMealTypeId, _binding.mealTypeChipGroup)
      updateChip(it.selectedDietTypeId, _binding.dietTypeChipGroup)
    })

    _binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->

      val selectedChip = group.findViewById<Chip>(checkedId)
      val selectedMealType = selectedChip.text.toString().lowercase(Locale.ROOT)

      _mealTypeChip = selectedMealType
      _mealTypeChipId = checkedId

    }

    _binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->

      val selectedChip = group.findViewById<Chip>(checkedId)
      val selectedDietType = selectedChip.text.toString().lowercase(Locale.ROOT)

      _dietTypeChip = selectedDietType
      _dietTypeChipId = checkedId

    }

    _binding.applyButton.setOnClickListener {
      // save the selected chips to our Data Store Preferences through the Recipes View Model
      _recipesViewModel.saveMealAndDietType(_mealTypeChip, _mealTypeChipId, _dietTypeChip, _dietTypeChipId)
    }

    return _binding.root
  }


  /**
   * updates the chip selection on the view
   */
  private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
    if (chipId != 0) {
      // we have previously made a selection and saved it
      try {
        chipGroup.findViewById<Chip>(chipId).isChecked = true
      } catch (e: Exception) {
        Log.d("RecipesBottomSheet", e.toString())
      }
    }
  }


  override fun onDestroy() {
    super.onDestroy()
    mBinding = null
  }

}