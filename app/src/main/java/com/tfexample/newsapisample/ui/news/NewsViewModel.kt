package com.tfexample.newsapisample.ui.news

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.data.NewsRepository
import com.tfexample.newsapisample.imageloaders.GrabImageLoader
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsViewModel @Inject constructor() : ViewModel() {
  @Inject
  lateinit var newsRepository: NewsRepository
  @Inject
  lateinit var grabImageLoader: GrabImageLoader

  private val compositeDisposable = CompositeDisposable()
  val newsListing: MutableLiveData<ArrayMap<Source, List<DBArticle>>> = MutableLiveData()
  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

  fun getNewsListing() {
    newsRepository.cachedNewsListing()?.let { result ->
      when {
        result.isEmpty -> fetchLatestNews()
        else -> newsListing.postValue(result)
      }
    } ?: run {
      fetchLatestNews()
    }
  }

  private fun fetchLatestNews() {
    compositeDisposable += newsRepository.fetchNewsListing().onErrorResumeNext {
      newsRepository.persistedArticles()
    }.doOnSubscribe { progressLiveData.postValue(true) }
        .subsIoObsMain()
        .subscribe { result, error ->
          progressLiveData.postValue(false)
          newsListing.postValue(result)
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

  fun switchFav(dbArticle: DBArticle) {
    compositeDisposable += newsRepository.updateArticle(
        dbArticle.copy(isFav = !dbArticle.isFav)).subsIoObsMain().subscribe { t1, t2 ->

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
