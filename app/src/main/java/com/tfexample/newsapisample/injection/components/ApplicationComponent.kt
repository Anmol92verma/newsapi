package com.tfexample.newsapisample.injection.components

import com.tfexample.newsapisample.imageloaders.GrabImageView
import com.tfexample.newsapisample.injection.modules.ActivityModule
import com.tfexample.newsapisample.injection.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun plusActivityComponent(activityModule: ActivityModule): ActivityComponent
  fun inject(grabImageView: GrabImageView)
}