package com.tfexample.newsapisample.data

import android.content.Context
import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.injection.ApplicationContext
import com.tfexample.newsapisample.networking.models.Article
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source
import io.reactivex.Single

class NewsDataHelper(@ApplicationContext private val application: Context) {

  fun persist(articles: List<Article>): Single<Unit> {
    return Single.fromCallable {
      NewsDatabaseProvider.getNewsDatabase(application).let { database ->
        database.sourcesDao().insertAllSources(articles.map { it.source }.map { source ->
          source?.let {
            it
          } ?: kotlin.run {
            getUnknownSource()
          }
        })
        database.articlesDao().insertAllArticles(articles.map {
          if (it.source == null) {
            it.copy(source = getUnknownSource())
          } else {
            it
          }
        }.map { it.toDbArticle(database.articlesDao().isFav(it.publishedAt)) })
      }
    }
  }

  private fun getUnknownSource(): Source {
    return Source("unknown", "unknown")
  }

  fun getPersistedArticles(): Single<ArrayMap<Source, List<DBArticle>>> {
    return Single.fromCallable {
      val database = NewsDatabaseProvider.getNewsDatabase(application)
      val sources = database.sourcesDao().getAllSources()
      val articles = ArrayMap<Source, List<DBArticle>>()
      sources.forEach { source ->
        database.articlesDao().getArticlesForSource(source.name).let {
          if (it.isNotEmpty()) {
            articles[source] = it
          }
        }
      }
      articles
    }
  }

  fun update(copy: DBArticle): Single<Unit> {
    return Single.fromCallable {
      val database = NewsDatabaseProvider.getNewsDatabase(application)
      database.articlesDao().update(copy)
    }
  }
}

private fun ArticlesDao.isFav(publishedAt: String): Boolean {
  return getArticleWith(publishedAt)?.isFav ?: false
}

private fun Article.toDbArticle(fav: Boolean): DBArticle {
  return DBArticle(this.author, this.content, this.description, this.publishedAt, this.source?.name,
      this.title, this.url, this.urlToImage, isFav = fav)
}
