package com.tfexample.anmolgrabassignment.injection.components

import com.tfexample.anmolgrabassignment.ui.news.ActivityNewsListing
import com.tfexample.anmolgrabassignment.injection.modules.ActivityModule
import com.tfexample.anmolgrabassignment.injection.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun inject(activity: ActivityNewsListing)
}