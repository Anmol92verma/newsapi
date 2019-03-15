package com.tfexample.newsapisample

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.tfexample.newsapisample.injection.ActivityContext
import com.tfexample.newsapisample.injection.ActivityScope
import dagger.Module
import dagger.Provides
import org.mockito.Mockito

@Module
class TestActivityModule(val activity: AppCompatActivity) {

  @Provides
  @ActivityScope
  @ActivityContext
  fun provideContext(): Context {
    return Mockito.mock(Context::class.java)
  }

}