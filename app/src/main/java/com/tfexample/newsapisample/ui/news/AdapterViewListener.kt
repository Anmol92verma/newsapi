package com.tfexample.newsapisample.ui.news

import com.tfexample.newsapisample.networking.models.DBArticle

interface AdapterViewListener {
  fun navigateTo(url: String?)
  fun switchFav(dbArticle: DBArticle)
}