package com.tfexample.anmolgrabassignment.ui.news

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.tfexample.anmolgrabassignment.R
import com.tfexample.anmolgrabassignment.databinding.ActivityMainBinding
import com.tfexample.anmolgrabassignment.injection.DaggerComponentManager
import com.tfexample.anmolgrabassignment.injection.components.ActivityComponent
import com.tfexample.anmolgrabassignment.ui.BaseActivity

class ActivityNewsListing : BaseActivity<ActivityMainBinding, ActNewsViewModel>() {

  private var newsAdapter: RvAdapterNewsListing? = null

  override fun getViewModelClass(): Class<ActNewsViewModel> {
    return ActNewsViewModel::class.java
  }

  override fun getLayoutId(): Int = R.layout.activity_main

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerComponentManager.getActivityComponent(this).inject(this)
  }

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
      newsAdapter = RvAdapterNewsListing()
      binding.rvNewsListing.adapter = newsAdapter
    }
    newsAdapter?.updateArticles(articles)
  }

}
