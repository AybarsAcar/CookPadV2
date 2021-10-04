package com.aybarsacar.cookpadversion2.utils

import androidx.recyclerview.widget.DiffUtil
import com.aybarsacar.cookpadversion2.models.Recipe


/**
 * used so when the data changes the whole list is not updated
 */
class RecipesDiffUtil(private val _oldList: List<Recipe>, private val _newList: List<Recipe>) : DiffUtil.Callback() {

  override fun getOldListSize(): Int {
    return _oldList.size
  }

  override fun getNewListSize(): Int {
    return _newList.size
  }

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return _oldList[oldItemPosition] === _newList[newItemPosition]
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return _oldList[oldItemPosition] == _newList[newItemPosition]
  }
}