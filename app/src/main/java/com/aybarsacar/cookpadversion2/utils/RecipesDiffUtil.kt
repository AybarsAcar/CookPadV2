package com.aybarsacar.cookpadversion2.utils

import androidx.recyclerview.widget.DiffUtil


/**
 * used so when the data changes the whole list is not updated
 * currently used in Ingredients and Recipe Type
 */
class RecipesDiffUtil<T>(private val _oldList: List<T>, private val _newList: List<T>) : DiffUtil.Callback() {

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