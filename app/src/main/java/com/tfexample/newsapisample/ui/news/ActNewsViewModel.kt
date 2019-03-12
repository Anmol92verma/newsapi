package com.tfexample.newsapisample.ui.news

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tfexample.newsapisample.dataproviders.NewsDataProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActNewsViewModel @Inject constructor(
    private val newsApiProvider: NewsDataProvider
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val newsListing: MutableLiveData<List<Article>> = MutableLiveData()
    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getNewsListing() {
        compositeDisposable += newsApiProvider.fetchNewsListing()
            .doOnSubscribe { progressLiveData.postValue(true) }
            .subsIoObsMain()
            .subscribe { result, error ->
                progressLiveData.postValue(false)
                result?.articles?.let {
                    newsListing.postValue(it)
                }
                error?.let {
                    it.printStackTrace()
                }
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

private fun <T> Single<T>.subsIoObsMain(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
