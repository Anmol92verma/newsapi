package com.tfexample.newsapisample.dataproviders

import android.content.Context
import com.tfexample.newsapisample.injection.ApplicationContext
import com.tfexample.newsapisample.ui.news.Article
import com.tfexample.newsapisample.ui.news.NewsListingModel
import io.reactivex.Single

class DataPersister(@ApplicationContext private val application: Context) {
    fun persist(articles: List<Article>): Single<Unit> {
        return Single.fromCallable {
            try {
                NewsDatabaseProvider.getNewsDatabase(application).let {
                    it.articlesDao().insertAll(articles)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun retrieve(): Single<List<Article>> {
        return Single.fromCallable {
            try {
                NewsDatabaseProvider.getNewsDatabase(application).let {
                    it.articlesDao().getAllArticles()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                listOf<Article>()
            }

        }

    }

    fun getPersistedArticles(): Single<NewsListingModel> {
        return Single.fromCallable {
            try {
                val dataSet = retrieve().blockingGet()
                NewsListingModel(dataSet, "Cached", dataSet.size)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                null
            }

        }

    }
}

/*private fun Article.nonNullRoomArticle(): Article {
    if (this.author.isNullOrEmpty()) {
        this.author = ""
    }
    if (this.content.isNullOrEmpty()) {
        this.content = ""
    }
}*/
