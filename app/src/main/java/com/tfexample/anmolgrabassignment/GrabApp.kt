package com.tfexample.anmolgrabassignment

import android.app.Application
import com.tfexample.anmolgrabassignment.injection.DaggerComponentManager

class GrabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DaggerComponentManager.initialize(this)
    }
}