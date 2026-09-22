package com.viltrumites.khuluma

import android.app.Application
import android.util.Log

class KhulumaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        Log.i("KhulumaApp", "Application started; services initialised")
    }
}
