package com.tfexample.newsapisample

import com.tfexample.newsapisample.imageloaders.GrabImageView
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestAppModule::class])
interface TestApplicationComponent {
  fun plusActivityComponent(activityModule: TestActivityModule): TestActivityComponent
  fun inject(grabImageView: GrabImageView)
}