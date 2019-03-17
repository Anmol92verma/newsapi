package com.tfexample.newsapisample.ui.news

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tfexample.newsapisample.data.NewsRepository
import com.tfexample.newsapisample.imageloaders.GrabImageLoader
import com.tfexample.newsapisample.networking.models.Article
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsViewModel @Inject constructor(): ViewModel() {
  @Inject lateinit var newsRepository: NewsRepository
  @Inject lateinit var grabImageLoader: GrabImageLoader

  private val compositeDisposable = CompositeDisposable()
  val newsListing: MutableLiveData<List<Article>> = MutableLiveData()
  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

  fun getNewsListing() {
    newsRepository.cachedNewsListing()?.let { result ->
      when {
        result.articles.isEmpty() -> fetchLatestNews()
        else -> result.articles.let {
          newsListing.postValue(it)
        }
      }
    } ?: run {
      fetchLatestNews()
    }
  }

  private fun fetchLatestNews() {
    compositeDisposable += newsRepository.fetchNewsListing()
        .doOnSubscribe { progressLiveData.postValue(true) }
        .subsIoObsMain()
        .subscribe { result, error ->
          progressLiveData.postValue(false)
          result?.articles?.let {
            newsListing.postValue(it)
          }
          error?.printStackTrace()
        }
  }

  override fun onCleared() {
    //destory
    grabImageLoader.destroyImageLoader()
    super.onCleared()
    compositeDisposable.let {
      if (!it.isDisposed) {
        it.dispose()
      }
    }
  }

}

fun <T> Single<T>.subsIoObsMain(): Single<T> {
  return this.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
}


fun <T> Observable<T>.subsIoObsMain(): Observable<T> {
  return this.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
}
