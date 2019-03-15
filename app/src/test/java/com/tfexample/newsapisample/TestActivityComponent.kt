package com.tfexample.newsapisample

import com.tfexample.newsapisample.injection.ActivityScope
import com.tfexample.newsapisample.ui.news.ActivityNewsListingGrab
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(TestActivityModule::class)])
interface TestActivityComponent {
  fun inject(activity: ActivityNewsListingGrab)
}