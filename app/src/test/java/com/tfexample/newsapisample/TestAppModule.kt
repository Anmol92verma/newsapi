package com.tfexample.newsapisample

import android.app.Application
import android.content.Context
import com.tfexample.newsapisample.dataproviders.DataPersister
import com.tfexample.newsapisample.dataproviders.NewsDataProvider
import com.tfexample.newsapisample.imageloaders.BufferedImageDownloader
import com.tfexample.newsapisample.imageloaders.GrabImageLoader
import com.tfexample.newsapisample.imageloaders.ImageProcessor
import com.tfexample.newsapisample.imageloaders.ImageRetriever
import com.tfexample.newsapisample.injection.ApplicationContext
import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.ui.news.Article
import com.tfexample.newsapisample.ui.news.NewsListingModel
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import okhttp3.OkHttpClient
import org.mockito.Mockito
import retrofit2.Retrofit

@Module
class TestAppModule(private val application: Application) {

  @Provides
  @ApplicationContext
  internal fun provideContext(): Context {
    return Mockito.mock(Application::class.java)
  }

  @Provides
  internal fun provideDataPersister(): DataPersister {
    val datapersister = Mockito.mock(DataPersister::class.java)
    Mockito.`when`(datapersister.getPersistedArticles()).thenReturn(Single.just(getTestNewsListingModel()))
    return datapersister
  }

  @Provides
  internal fun providesOkHttpClient(): OkHttpClient {
    return Mockito.mock(OkHttpClient::class.java)
  }

  @Provides
  internal fun providesBufferedImageDownloader(
      okHttpClient: OkHttpClient): BufferedImageDownloader {
    return Mockito.mock(BufferedImageDownloader::class.java)
  }

  @Provides
  internal fun provideImageRetriever(downloader: BufferedImageDownloader,
      imageProcessor: ImageProcessor): ImageRetriever {
    return Mockito.mock(ImageRetriever::class.java)
  }

  @Provides
  internal fun providesImageProvider(): ImageProcessor {
    return Mockito.mock(ImageProcessor::class.java)
  }

  @Provides
  internal fun providesGrabImageLoader(imageRetriever: ImageRetriever): GrabImageLoader {
    return Mockito.mock(GrabImageLoader::class.java)
  }

  @Provides
  internal fun providesRetrofit(): Retrofit {
    return Mockito.mock(Retrofit::class.java)
  }

  @Provides
  internal fun providesNewsApiService(retrofit: Retrofit): NewsApiService {
    val apiService = Mockito.mock(NewsApiService::class.java)
    Mockito.`when`(apiService.getNewsListing()).thenReturn(Single.just(getTestNewsListingModel()))
    return apiService
  }

  private fun getTestNewsListingModel(): NewsListingModel? {
    val article = Article("author", "someContent", "desc", "39293", null, "title", "url", "urlll")
    return NewsListingModel(listOf(article), "Err", 1)
  }

  @Provides
  internal fun provideNewsDataProvider(apiService: NewsApiService,
      dataPersister: DataPersister): NewsDataProvider {
    return Mockito.mock(NewsDataProvider::class.java)
  }
}