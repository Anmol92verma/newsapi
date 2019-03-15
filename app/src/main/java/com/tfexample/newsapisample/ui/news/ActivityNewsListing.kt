package com.tfexample.newsapisample.ui.news

import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.tfexample.newsapisample.AdapterViewListener
import com.tfexample.newsapisample.GridSpacingItemDecoration
import com.tfexample.newsapisample.R
import com.tfexample.newsapisample.databinding.ActivityMainBinding
import com.tfexample.newsapisample.dataproviders.NewsDataProvider
import com.tfexample.newsapisample.injection.components.ActivityComponent
import com.tfexample.newsapisample.ui.BaseActivity
import javax.inject.Inject

class ActivityNewsListing : BaseActivity<ActivityMainBinding, ActNewsViewModel>(), AdapterViewListener {

  private var newsAdapter: RvAdapterNewsListing? = null

  @Inject lateinit var newsApiProvider: NewsDataProvider

  override fun getViewModelClass(): Class<ActNewsViewModel> {
    return ActNewsViewModel::class.java
  }

  override fun getLayoutId(): Int = com.tfexample.newsapisample.R.layout.activity_main

  override fun onComponentCreated(component: ActivityComponent) = component.inject(this)

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    listenObservers()
    viewModel.setNewsDataProvider(newsApiProvider)
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
      binding.rvNewsListing.layoutManager = StaggeredGridLayoutManager(2,
          StaggeredGridLayoutManager.VERTICAL)
      val decoration = GridSpacingItemDecoration(2,
          resources.getDimension(R.dimen.dimen_4dp).toInt(), true, 0)
      binding.rvNewsListing.addItemDecoration(decoration)
      newsAdapter = RvAdapterNewsListing(this)
      binding.rvNewsListing.adapter = newsAdapter
    }
    newsAdapter?.updateArticles(articles)
  }

  override fun navigateTo(url: String?) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this@ActivityNewsListing,
        com.tfexample.newsapisample.R.color.colorAccent))
    builder.addDefaultShareMenuItem()

    val anotherCustomTab = CustomTabsIntent.Builder().build()


    val bitmap = BitmapFactory.decodeResource(resources,
        com.tfexample.newsapisample.R.drawable.ic_launcher_foreground)
    val requestCode = 100
    val intent = anotherCustomTab.intent
    intent.data = Uri.parse(url)

    val pendingIntent = PendingIntent.getActivity(this@ActivityNewsListing,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    builder.setActionButton(bitmap, "Android", pendingIntent, true)
    builder.setShowTitle(true)


    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this@ActivityNewsListing, Uri.parse(url))
  }

}
