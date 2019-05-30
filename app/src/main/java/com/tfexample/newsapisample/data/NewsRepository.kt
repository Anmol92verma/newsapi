package com.tfexample.newsapisample.data

import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source
import io.reactivex.Single
import io.reactivex.SingleSource

class NewsRepository constructor(
    private val newsApiService: NewsApiService,
    private val newsDataHelper: NewsDataHelper
) {
  private var cachedNewsModel: ArrayMap<Source, List<DBArticle>>? = null

  fun fetchNewsListing(): Single<ArrayMap<Source, List<DBArticle>>> {
    return newsApiService.getNewsListing().flatMap { newsModel ->
      newsDataHelper.persist(newsModel.articles).map { newsModel }
    }.flatMap {
      persistedArticles()
    }
  }

  fun persistedArticles(): SingleSource<out ArrayMap<Source, List<DBArticle>>>? {
    return newsDataHelper.getPersistedArticles().doOnSuccess {
      this.cachedNewsModel = it
    }
  }

  fun cachedNewsListing(): ArrayMap<Source, List<DBArticle>>? {
    return cachedNewsModel
  }

  fun updateArticle(copy: DBArticle): Single<Unit> {
    return newsDataHelper.update(copy)
  }

}