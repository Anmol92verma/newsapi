package com.tfexample.newsapisample.imageloaders

import android.net.Uri
import android.support.v4.util.ArrayMap
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashSet

private const val MAX_CONCURRENT_ITEMS = 5

class ImageRetriever(private val downloader: BufferedImageDownloader) {
  private val priorityList = Stack<Pair<String, Disposable?>>()
  private val queuedItems = Stack<Pair<String, Disposable?>>()

  val processedImageUrls = HashSet<String>()
  val listeners = ArrayMap<String, OnImageAvailableListener>()
  private var dimens: Pair<Int, Int>? = null

  fun retrieveFor(imageUri: Uri,
      dimens: Pair<Int, Int>) {
    this.dimens = dimens
    if (pendingRequestsContains(imageUri)) {
      removeFromAllqueues(imageUri)
    }
    queuedItems.push(Pair(imageUri.toString(), null))
    Log.e("ImageRetriever", "added $imageUri to queue")
    processPriorityList()
  }

  private fun processPriorityList() {
    if (queuedItems.isNotEmpty()) {
      // take the latest item from queuedItems
      val currentJobItem = queuedItems.pop()
      currentJobItem?.let {
        addToJobs(it)
      }
    }
  }

  private fun addToJobs(
      currentJobItem: Pair<String, Disposable?>) {
    currentJobItem.let { lastItem ->
      if (priorityList.size < MAX_CONCURRENT_ITEMS) {
        // and move it to priority list if priorityList size < 5
        priorityList.add(currentJobItem)
        listeners.first()?.let { imageListener ->
          downloader.downloadFileByOkio(lastItem.first,dimens)
              .countedThreadScheduler(MAX_CONCURRENT_ITEMS)
              .doOnSubscribe {
                currentJobItem.second = it
              }
              .subscribe({ progress ->
                listeners[lastItem.first]?.onImageProgress(Uri.parse(lastItem.first), progress)
              }, {
                it.printStackTrace()
                Log.e("GrabIV", "errrrr for ${lastItem.first}")
                listeners[lastItem.first]?.onImageError(Uri.parse(lastItem.first))
              }, {
                priorityList.remove(currentJobItem)
                processPriorityList()
                currentJobItem.first.let {
                  putEntry(it)
                }
              })
        }

      }
    }
  }

  fun removeFromAllqueues(imageUri: Uri?) {
    imageUri?.let { uri ->
      queuedItems.find { it.first.equals(uri.toString()) }?.let { pair ->
        pair.second?.let {
          it.dispose()
        }
        Log.e("ImageRetriever", "queuedItems removed for ${pair.first}")
        queuedItems.remove(pair)
      }
      priorityList.find { it.first.equals(uri.toString()) }?.let { pair ->
        pair.second?.let {
          it.dispose()
        }
        Log.e("ImageRetriever", "priorityList removed for ${pair.first}")
        priorityList.remove(pair)
      }
    }
  }

  private fun pendingRequestsContains(uri: Uri): Boolean {
    queuedItems.find { it.first.equals(uri.toString()) }?.let { pair ->
      return true
    }
    priorityList.find { it.first.equals(uri.toString()) }?.let { pair ->
      return true
    }

    return false
  }

  private fun putEntry(url: String) {
    processedImageUrls.add(url)
    listeners.forEach {
      it.value.onImageAvailable(Uri.parse(url))
    }
  }

  fun removeListener(imageUri: Uri?) {
    imageUri?.let {
      if (this.listeners.containsKey(imageUri.toString())) {
        this.listeners.remove(imageUri.toString())
      }
    }
  }

  fun addListener(grabImageView: OnImageAvailableListener,
      it: Uri) {
    if (!this.listeners.containsKey(it.toString())) {
      this.listeners[it.toString()] = grabImageView
    }
  }

  fun canAddRequest(uri: Uri): Boolean {
    return !isProcessing(uri) && !hasProcessed(uri)
  }

  fun hasProcessed(uri: Uri): Boolean {
    return this.processedImageUrls.contains(uri.toString())
  }

  private fun isProcessing(uri: Uri): Boolean {
    return pendingRequestsContains(uri)
  }
}

private fun <K, V> ArrayMap<K, V>.first(): V? {
  return this[this.keys.first()]
}

data class Pair<A, B>(
    var first: A,
    var second: B
)

private fun <T> Observable<T>.countedThreadScheduler(numthreads: Int): Observable<T> {
  return this.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(numthreads)))
}
