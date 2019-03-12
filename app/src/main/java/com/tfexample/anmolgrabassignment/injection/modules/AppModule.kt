package com.tfexample.anmolgrabassignment.injection.modules

import android.app.Application
import android.content.Context
import com.tfexample.anmolgrabassignment.BASE_URL_NEWS_API
import com.tfexample.anmolgrabassignment.dataproviders.NewsDataProvider
import com.tfexample.anmolgrabassignment.injection.ApplicationContext
import com.tfexample.anmolgrabassignment.networking.NewsApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class AppModule(private val application: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return application
    }

    @Provides
    internal fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    internal fun providesNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    internal fun provideNewsDataProvider(apiService: NewsApiService): NewsDataProvider {
        return NewsDataProvider(apiService)
    }
}