package com.tfexample.newsapisample.dataproviders

import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.ui.news.NewsListingModel
import io.reactivex.Single

class NewsDataProvider constructor(
    private val newsApiService: NewsApiService,
    private val dataPersister: DataPersister
) {
  private var cachedNewsModel: NewsListingModel? = null

  fun fetchNewsListing(): Single<NewsListingModel> {
    return newsApiService.getNewsListing().flatMap { newsModel ->
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