package com.tfexample.newsapisample.data

import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.networking.models.NewsListingModel
import io.reactivex.Single

class NewsRepository constructor(
    private val newsApiService: NewsApiService,
    private val newsDataHelper: NewsDataHelper
) {
  private var cachedNewsModel: NewsListingModel? = null

  fun fetchNewsListing(): Single<NewsListingModel> {
    return newsApiService.getNewsListing().flatMap { newsModel ->
      this.cachedNewsModel = newsModel
      newsDataHelper.persist(newsModel.articles).map { newsModel }
    }.onErrorResumeNext {
      newsDataHelper.getPersistedArticles()
    }
  }

  fun cachedNewsListing(): NewsListingModel? {
    return cachedNewsModel
  }

}