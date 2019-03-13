package com.tfexample.newsapisample.ui.news

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.tfexample.newsapisample.AdapterViewListener
import com.tfexample.newsapisample.R
import com.tfexample.newsapisample.databinding.ActivityMainBinding
import com.tfexample.newsapisample.injection.components.ActivityComponent
import com.tfexample.newsapisample.ui.BaseActivity
import okhttp3.OkHttpClient
import javax.inject.Inject

class ActivityNewsListing : BaseActivity<ActivityMainBinding, ActNewsViewModel>(), AdapterViewListener {

  private var newsAdapter: RvAdapterNewsListing? = null

  @Inject
  lateinit var okHttpClient: OkHttpClient

  override fun getViewModelClass(): Class<ActNewsViewModel> {
    return ActNewsViewModel::class.java
  }

  override fun getLayoutId(): Int = R.layout.activity_main

  override fun onComponentCreated(component: ActivityComponent) = component.inject(this)

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    listenObservers()
    viewModel.getNewsListing()
  }

  private fun listenObservers() {
    viewModel.newsListing.observe(this, Observer { articles ->
      articles?.let {
        updateNewsAdapter(it)
      }
    })
    viewModel.progressLiveData.observe(this, Observer {
      it?.let { showProgress ->
        if (showProgress) {
          binding.progressBar.visibility = View.VISIBLE
        } else {
          binding.progressBar.visibility = View.GONE
        }
      }
    })
  }


  private fun updateNewsAdapter(articles: List<Article>) {
    if (newsAdapter == null) {
      newsAdapter = RvAdapterNewsListing(this)
      binding.rvNewsListing.adapter = newsAdapter
    }
    newsAdapter?.updateArticles(articles)
  }

  override fun provideOkHttpClient(): OkHttpClient {
    return okHttpClient
  }

}
