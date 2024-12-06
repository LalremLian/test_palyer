package com.example.testapp.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {
     override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .build()
    }
}