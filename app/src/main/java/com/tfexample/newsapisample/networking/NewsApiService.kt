package com.tfexample.newsapisample.networking

import com.tfexample.newsapisample.ui.utils.API_KEY_NEWS_API
import com.tfexample.newsapisample.ui.utils.COUNTRY
import com.tfexample.newsapisample.ui.utils.KEY_API
import com.tfexample.newsapisample.ui.utils.KEY_COUNTRY
import com.tfexample.newsapisample.networking.models.NewsListingModel
import io.reactivex.Single
import retrofit2.http.GET


interface NewsApiService {
  @GET("top-headlines?$KEY_COUNTRY=$COUNTRY&$KEY_API=$API_KEY_NEWS_API")
  fun getNewsListing(): Single<NewsListingModel>

}