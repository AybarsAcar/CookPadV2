package com.aybarsacar.cookpadversion2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/**
 * entry point to our application
 * our application will be using Hilt for dependency injection
 */
@HiltAndroidApp
class MyApplication : Application() {
}