package com.tfexample.newsapisample

import android.support.v7.app.AppCompatActivity
import com.tfexample.newsapisample.GrabApp
import com.tfexample.newsapisample.injection.components.ActivityComponent
import com.tfexample.newsapisample.injection.components.ApplicationComponent
import com.tfexample.newsapisample.injection.components.DaggerApplicationComponent
import com.tfexample.newsapisample.injection.modules.ActivityModule
import com.tfexample.newsapisample.injection.modules.AppModule

class TestDaggerComponentManager {

  companion object {
    var appComponent: TestApplicationComponent? = null
    fun initialize(application: TestGrabApp) {
      appComponent = DaggerTestApplicationComponent.builder().testAppModule(
          TestAppModule(
              application
          )
      ).build()
    }

    fun getActivityComponent(activity: AppCompatActivity): TestActivityComponent {
      return appComponent?.plusActivityComponent(
          TestActivityModule(activity)
      ) ?: throw IllegalStateException(
          "App Component is not initialized"
      )
    }
  }


}