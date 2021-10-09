package com.aybarsacar.cookpadversion2.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.aybarsacar.cookpadversion2.databinding.FragmentInstructionsBinding
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.utils.Constants


class InstructionsFragment : Fragment() {

  private var mBinding: FragmentInstructionsBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val _binding get() = mBinding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mBinding = FragmentInstructionsBinding.inflate(inflater, container, false)

    val args = arguments
    val currentRecipe: Recipe? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

    _binding.instructionsWebView.webViewClient = object : WebViewClient() {}

    currentRecipe?.let {
      _binding.instructionsWebView.loadUrl(it.sourceUrl)
    }

    return _binding.root
  }

  override fun onDestroy() {
    super.onDestroy()
    mBinding = null
  }
}