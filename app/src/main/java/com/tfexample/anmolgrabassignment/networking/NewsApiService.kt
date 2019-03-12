package com.tfexample.anmolgrabassignment.networking

import android.support.v4.util.ArrayMap
import com.tfexample.anmolgrabassignment.ui.news.NewsListingModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface NewsApiService {
    @GET("top-headlines")
    fun getNewsListing(@QueryMap queryMap: ArrayMap<String, String>): Single<NewsListingModel>

}