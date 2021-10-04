package com.aybarsacar.cookpadversion2.ui.fragments.foodjokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aybarsacar.cookpadversion2.R


class FoodJokeFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_food_joke, container, false)
  }

}