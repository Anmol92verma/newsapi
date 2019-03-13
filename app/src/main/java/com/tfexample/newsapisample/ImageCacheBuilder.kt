package com.tfexample.newsapisample

import android.net.Uri
import android.support.v4.util.ArrayMap
import android.util.Log
import android.webkit.URLUtil
import com.tfexample.newsapisample.ui.news.subsIoObsMain
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.Executors

object ImageCacheBuilder {
  private val priorityQueue = ArrayMap<String, Pair<OnImageAvailableListener, Disposable?>>()

  fun retrieveFor(imageUri: Uri,
      grabImageView: OnImageAvailableListener) {
    if (priorityQueue.containsKey(imageUri.toString())) {
      priorityQueue.remove(imageUri.toString())
    }
    priorityQueue[imageUri.toString()] = Pair(grabImageView, null)
    continueWork()
  }

  fun getCacheFile(forUrl: String, cacheDir: File): File {
    val fileNameWithExtension = URLUtil.guessFileName(forUrl, null, null);
    return File(cacheDir, fileNameWithExtension)
  }

  private fun continueWork() {
    priorityQueue.keys.takeIf { it.isNotEmpty() }?.let {
      val key = it.first()
      priorityQueue[key]?.let { item ->
        val file = getCacheFile(key, item.first.getCacheDir())
        item.first.getHttpClient()?.let { it ->
          downloadFileByOkio(key, file, it)
              .subsIoObsMain()
              .doOnSubscribe {
                priorityQueue[key] = Pair(item.first, it)
              }
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({
                Log.d("GrabIV", "$it for ${key}")
              }, {
                it.printStackTrace()
              }, {
                item.first.onImageAvailable(Uri.fromFile(file))
                if (priorityQueue.contains(key)) {
                  priorityQueue.remove(key)
                }
              })
        }
      }
    }
  }

  fun removeRequest(imageUri: Uri?) {
    imageUri?.let {
      if (priorityQueue.containsKey(it.toString())) {
        priorityQueue[it.toString()]?.second?.let {
          if (!it.isDisposed) {
            it.dispose()
          }
        }
        priorityQueue.remove(it.toString())
      }
    }
  }
}

private fun <T> Observable<T>.countedThreadScheduler(numthreads: Int): Observable<T> {
  return this.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(numthreads)))
}
