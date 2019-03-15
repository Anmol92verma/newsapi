package com.tfexample.newsapisample.injection

import android.support.v7.app.AppCompatActivity
import com.tfexample.newsapisample.GrabApp
import com.tfexample.newsapisample.injection.components.ActivityComponent
import com.tfexample.newsapisample.injection.components.ApplicationComponent
import com.tfexample.newsapisample.injection.components.DaggerApplicationComponent
import com.tfexample.newsapisample.injection.modules.ActivityModule
import com.tfexample.newsapisample.injection.modules.AppModule

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