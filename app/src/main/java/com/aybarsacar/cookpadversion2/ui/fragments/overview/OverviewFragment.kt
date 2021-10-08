package com.aybarsacar.cookpadversion2.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.databinding.FragmentOverviewBinding
import com.aybarsacar.cookpadversion2.models.Recipe
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {

  private var mBinding: FragmentOverviewBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!


  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mBinding = FragmentOverviewBinding.inflate(inflater, container, false)

    val args = arguments
    val currentRecipe: Recipe? = args?.getParcelable("recipeBundle")

    currentRecipe?.let {
      _binding.mainImageView.load(it.image)
      _binding.titleTextView.text = it.title
      _binding.likesTextView.text = it.aggregateLikes.toString()
      _binding.timeTextView.text = it.readyInMinutes.toString()
      _binding.summaryTextView.text = Jsoup.parse(it.summary).text()

      if (it.vegetarian) {
        _binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }

      if (it.vegan) {
        _binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }

      if (it.glutenFree) {
        _binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }

      if (it.dairyFree) {
        _binding.dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }

      if (it.veryHealthy) {
        _binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }

      if (it.cheap) {
        _binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        _binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
      }
    }

    return _binding.root
  }


  override fun onDestroyView() {
    super.onDestroyView()
    mBinding = null
  }

}