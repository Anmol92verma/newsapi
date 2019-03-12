package com.tfexample.newsapisample.dataproviders

import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.API_KEY_NEWS_API
import com.tfexample.newsapisample.COUNTRY
import com.tfexample.newsapisample.KEY_API
import com.tfexample.newsapisample.KEY_COUNTRY
import com.tfexample.newsapisample.networking.NewsApiService
import com.tfexample.newsapisample.ui.news.NewsListingModel
import io.reactivex.Single
import javax.inject.Inject

class NewsDataProvider @Inject constructor(private val newsApiService: NewsApiService) {

    private val queryMapNews by lazy {
        val map = ArrayMap<String, String>()
        map[KEY_COUNTRY] = COUNTRY
        map[KEY_API] = API_KEY_NEWS_API
        map
    }

    fun fetchNewsListing(): Single<NewsListingModel> {
        return newsApiService.getNewsListing(queryMapNews)
    }

}