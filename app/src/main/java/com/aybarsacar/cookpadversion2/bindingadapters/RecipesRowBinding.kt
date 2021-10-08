package com.aybarsacar.cookpadversion2.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.aybarsacar.cookpadversion2.R
import com.aybarsacar.cookpadversion2.models.Recipe
import com.aybarsacar.cookpadversion2.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup


class RecipesRowBinding {
  companion object {


    @BindingAdapter("onRecipeCLickListener")
    @JvmStatic
    fun onRecipeCLickListener(recipeRowLayout: ConstraintLayout, recipe: Recipe) {
      recipeRowLayout.setOnClickListener {
        try {

          // pass in the recipe as a safe arg to the navigation
          val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(recipe)

          recipeRowLayout.findNavController().navigate(action)
        } catch (e: Exception) {
          Log.d("onRecipeClickListener", e.toString())
        }
      }
    }


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


    @BindingAdapter("parseHtml")
    @JvmStatic
    fun parseHtml(textView: TextView, text: String?) {

      text?.let {
        val parsedText = Jsoup.parse(it).text()
        textView.text = parsedText
      }
    }
  }
}