package com.tfexample.newsapisample.dataproviders

import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.API_KEY_NEWS_API
import com.tfexample.newsapisample.COUNTRY
import com.tfexample.newsapisample.KEY_API
import com.tfexample.newsapisample.KEY_COUNTRY
import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.ui.news.NewsListingModel
import io.reactivex.Single

class NewsDataProvider constructor(
    private val newsApiService: NewsApiService,
    private val dataPersister: DataPersister
) {
  private var cachedNewsModel: NewsListingModel? = null

  private val queryMapNews by lazy {
    val map = ArrayMap<String, String>()
    map[KEY_COUNTRY] = COUNTRY
    map[KEY_API] = API_KEY_NEWS_API
    map
  }

  fun fetchNewsListing(): Single<NewsListingModel> {
    return newsApiService.getNewsListing(queryMapNews).flatMap { newsModel ->
      this.cachedNewsModel = newsModel
      dataPersister.persist(newsModel.articles).map { newsModel }
    }.onErrorResumeNext {
      dataPersister.getPersistedArticles()
    }
  }

  fun cachedNewsListing(): NewsListingModel? {
    return cachedNewsModel
  }

}