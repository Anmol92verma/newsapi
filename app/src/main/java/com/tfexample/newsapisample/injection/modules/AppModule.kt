package com.tfexample.newsapisample.injection.modules

import android.app.Application
import android.content.Context
import com.tfexample.newsapisample.BASE_URL_NEWS_API
import com.tfexample.newsapisample.dataproviders.DataPersister
import com.tfexample.newsapisample.dataproviders.NewsDataProvider
import com.tfexample.newsapisample.imageloaders.BufferedImageDownloader
import com.tfexample.newsapisample.imageloaders.GrabImageLoader
import com.tfexample.newsapisample.imageloaders.ImageRetriever
import com.tfexample.newsapisample.imageloaders.ImageProcessor
import com.tfexample.newsapisample.injection.ApplicationContext
import com.tfexample.newsapisample.networking.NewsApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
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
  internal fun provideDataPersister(): DataPersister {
    return DataPersister(application)
  }

  @Provides
  internal fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient()
  }

  @Provides
  internal fun providesBufferedImageDownloader(
      okHttpClient: OkHttpClient): BufferedImageDownloader {
    return BufferedImageDownloader(okHttpClient, application)
  }

  @Provides
  internal fun provideImageRetriever(downloader: BufferedImageDownloader,
      imageProcessor: ImageProcessor): ImageRetriever {
    return ImageRetriever(downloader, imageProcessor)
  }

  @Provides
  internal fun providesImageProvider(): ImageProcessor {
    return ImageProcessor(application)
  }

  @Provides
  internal fun providesGrabImageLoader(imageRetriever: ImageRetriever): GrabImageLoader {
    return GrabImageLoader(application, imageRetriever)
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
  internal fun provideNewsDataProvider(apiService: NewsApiService,
      dataPersister: DataPersister): NewsDataProvider {
    return NewsDataProvider(apiService, dataPersister)
  }
}