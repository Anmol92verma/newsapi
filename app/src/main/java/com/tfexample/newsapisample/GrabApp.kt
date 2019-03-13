package com.tfexample.newsapisample

import android.app.Application
import com.tfexample.newsapisample.injection.DaggerComponentManager

class NewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DaggerComponentManager.initialize(this)
    }
}