package com.tfexample.anmolgrabassignment.dataproviders

import android.support.v4.util.ArrayMap
import com.tfexample.anmolgrabassignment.API_KEY_NEWS_API
import com.tfexample.anmolgrabassignment.COUNTRY
import com.tfexample.anmolgrabassignment.KEY_API
import com.tfexample.anmolgrabassignment.KEY_COUNTRY
import com.tfexample.anmolgrabassignment.networking.NewsApiService
import com.tfexample.anmolgrabassignment.ui.news.NewsListingModel
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