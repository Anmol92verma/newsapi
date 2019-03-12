package com.tfexample.anmolgrabassignment.injection

import android.support.v7.app.AppCompatActivity
import com.tfexample.anmolgrabassignment.GrabApp
import com.tfexample.anmolgrabassignment.injection.components.ActivityComponent
import com.tfexample.anmolgrabassignment.injection.components.ApplicationComponent
import com.tfexample.anmolgrabassignment.injection.components.DaggerApplicationComponent
import com.tfexample.anmolgrabassignment.injection.modules.ActivityModule
import com.tfexample.anmolgrabassignment.injection.modules.AppModule

class DaggerComponentManager {

    companion object {
        var appComponent: ApplicationComponent? = null
        fun initialize(application: GrabApp) {
            appComponent = DaggerApplicationComponent.builder().appModule(
                AppModule(
                    application
                )
            ).build()
        }

        fun getActivityComponent(activity: AppCompatActivity): ActivityComponent {
            return appComponent?.plusActivityComponent(
                ActivityModule(activity)
            ) ?: throw IllegalStateException(
                "App Component is not initialized"
            )
        }
    }


}