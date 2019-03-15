package com.tfexample.newsapisample.ui.news

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tfexample.newsapisample.dataproviders.NewsDataProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class ActNewsViewModel : ViewModel() {

  private val compositeDisposable = CompositeDisposable()
  val newsListing: MutableLiveData<List<Article>> = MutableLiveData()
  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()
  private lateinit var newsDataProvider: NewsDataProvider

  fun setNewsDataProvider(newsApiProvider: NewsDataProvider) {
    this.newsDataProvider = newsApiProvider
  }

  fun getNewsListing() {
    newsDataProvider.cachedNewsListing()?.let { result ->
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
    compositeDisposable += newsDataProvider.fetchNewsListing()
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
