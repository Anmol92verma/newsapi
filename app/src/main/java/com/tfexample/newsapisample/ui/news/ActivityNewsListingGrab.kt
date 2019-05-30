package com.tfexample.newsapisample.ui.news

import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.util.ArrayMap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.tfexample.newsapisample.R
import com.tfexample.newsapisample.databinding.ActivityMainBinding
import com.tfexample.newsapisample.injection.components.ActivityComponent
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source
import com.tfexample.newsapisample.ui.GrabBaseActivity
import com.tfexample.newsapisample.ui.utils.GridSpacingItemDecoration

class ActivityNewsListingGrab : GrabBaseActivity<ActivityMainBinding, NewsViewModel>(), AdapterViewListener {

  private var newsAdapter: RvAdapterNewsListing? = null

  override fun getViewModelClass(): Class<NewsViewModel> {
    return NewsViewModel::class.java
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


  private fun updateNewsAdapter(
      articles: ArrayMap<Source, List<DBArticle>>) {
    if (newsAdapter == null) {
      if (!resources.getBoolean(R.bool.isTablet)) {
        binding.rvNewsListing.layoutManager = LinearLayoutManager(this)
        val decoration = GridSpacingItemDecoration(1,
            resources.getDimension(R.dimen.dimen_4dp).toInt(), true, 0)
        binding.rvNewsListing.addItemDecoration(decoration)
      } else {
        binding.rvNewsListing.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        val decoration = GridSpacingItemDecoration(2,
            resources.getDimension(R.dimen.dimen_4dp).toInt(), true, 0)
        binding.rvNewsListing.addItemDecoration(decoration)
      }
      newsAdapter = RvAdapterNewsListing(this)
      binding.rvNewsListing.adapter = newsAdapter
    }
    newsAdapter?.updateArticles(articles)
  }

  override fun switchFav(dbArticle: DBArticle) {
    viewModel.switchFav(dbArticle)
  }

  override fun navigateTo(url: String?) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this@ActivityNewsListingGrab,
        com.tfexample.newsapisample.R.color.colorAccent))
    builder.addDefaultShareMenuItem()

    val anotherCustomTab = CustomTabsIntent.Builder().build()


    val bitmap = BitmapFactory.decodeResource(resources,
        com.tfexample.newsapisample.R.drawable.ic_launcher_foreground)
    val requestCode = 100
    val intent = anotherCustomTab.intent
    intent.data = Uri.parse(url)

    val pendingIntent = PendingIntent.getActivity(this@ActivityNewsListingGrab,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    builder.setActionButton(bitmap, "Android", pendingIntent, true)
    builder.setShowTitle(true)


    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this@ActivityNewsListingGrab, Uri.parse(url))
  }

}
