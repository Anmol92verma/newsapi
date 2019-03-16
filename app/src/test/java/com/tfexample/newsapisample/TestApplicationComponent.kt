package com.tfexample.newsapisample

import com.tfexample.newsapisample.imageloaders.GrabImageView
import com.tfexample.newsapisample.injection.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class,ViewModelModule::class])
interface TestApplicationComponent {
  fun plusActivityComponent(activityModule: TestActivityModule): TestActivityComponent
  fun inject(grabImageView: GrabImageView)
}