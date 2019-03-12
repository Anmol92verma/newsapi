package com.tfexample.newsapisample.injection.components

import com.tfexample.newsapisample.ui.news.ActivityNewsListing
import com.tfexample.newsapisample.injection.modules.ActivityModule
import com.tfexample.newsapisample.injection.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(activity: ActivityNewsListing)
}