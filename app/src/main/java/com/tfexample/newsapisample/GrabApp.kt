package com.tfexample.newsapisample

import android.app.Application
import com.tfexample.newsapisample.injection.DaggerComponentManager

class GrabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DaggerComponentManager.initialize(this)
    }
}