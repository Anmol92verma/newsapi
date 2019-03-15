package com.tfexample.newsapisample.injection.modules

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.tfexample.newsapisample.injection.ActivityContext
import com.tfexample.newsapisample.injection.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: AppCompatActivity) {

  @Provides
  @ActivityScope
  @ActivityContext
  fun provideContext(): Context {
    return activity
  }

}