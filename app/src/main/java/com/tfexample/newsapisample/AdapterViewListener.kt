package com.tfexample.newsapisample

import okhttp3.OkHttpClient

interface AdapterViewListener {
  fun provideOkHttpClient(): OkHttpClient
  fun navigateTo(url: String?)
}