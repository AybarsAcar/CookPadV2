package com.aybarsacar.cookpadversion2.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class PagerAdapter(
  private val _resultBundle: Bundle,
  private val _fragments: ArrayList<Fragment>,
  private val _titles: ArrayList<String>,
  _fragmentManager: FragmentManager
) : FragmentPagerAdapter(_fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  override fun getCount(): Int {
    return _fragments.size
  }

  override fun getItem(position: Int): Fragment {
    _fragments[position].arguments = _resultBundle
    return _fragments[position]
  }


  override fun getPageTitle(position: Int): CharSequence? {
    return _titles[position]
  }
}