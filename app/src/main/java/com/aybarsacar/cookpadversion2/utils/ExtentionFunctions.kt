package com.aybarsacar.cookpadversion2.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * live data extension function to observe hte live data object only once not every time
 * it is used to prevent calling the local database after a new request is fetched from the api
 *
 * Otherwise, it makes 2 request when we need call our API - one to the API, one to the local data
 * which is the behaviour we want to prevent with this extension function
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {

  observe(lifecycleOwner, object : Observer<T> {

    override fun onChanged(t: T) {
      removeObserver(this)
      observer.onChanged(t)
    }

  })

}