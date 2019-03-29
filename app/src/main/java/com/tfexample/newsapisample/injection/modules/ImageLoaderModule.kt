package com.tfexample.newsapisample.injection.modules

import android.app.Application
import com.tfexample.newsapisample.imageloaders.BufferedImageDownloader
import com.tfexample.newsapisample.imageloaders.GrabImageLoader
import com.tfexample.newsapisample.imageloaders.ImageProcessor
import com.tfexample.newsapisample.imageloaders.ImageRetriever
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class ImageLoaderModule(private val application: Application) {

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
}