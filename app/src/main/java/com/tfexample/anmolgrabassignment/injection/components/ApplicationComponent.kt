package com.tfexample.anmolgrabassignment.injection.components

import com.tfexample.anmolgrabassignment.injection.modules.ActivityModule
import com.tfexample.anmolgrabassignment.injection.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun plusActivityComponent(activityModule: ActivityModule): ActivityComponent
}