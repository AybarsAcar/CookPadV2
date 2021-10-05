package com.aybarsacar.cookpadversion2.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.aybarsacar.cookpadversion2.R


class RecipesRowBinding {
  companion object {

    @BindingAdapter("setNumberOfLikes")
    @JvmStatic
    fun setNumberOfLikes(textView: TextView, likes: Int) {
      textView.text = likes.toString()
    }


    @BindingAdapter("setNumberOfMinutes")
    @JvmStatic
    fun setNumberOfMinutes(textView: TextView, minutes: Int) {
      textView.text = minutes.toString()
    }


    /**
     * we can use the following in any type of view
     */
    @BindingAdapter("applyVeganStyle")
    @JvmStatic
    fun applyVeganStyle(view: View, isVegan: Boolean) {
      if (isVegan) {
        when (view) {
          is TextView -> {
            view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
          }

          is ImageView -> {
            view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
          }
        }
      }
    }


    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(view: View, imageUrl: String) {

      when (view) {
        is ImageView -> {
          view.load(imageUrl) {
            crossfade(600)

            // handle the error case - default image view when the images are not downloaded
            error(R.drawable.ic_no_photos)
          }
        }
      }

    }

  }
}