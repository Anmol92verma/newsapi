package com.tfexample.newsapisample.imageloaders

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Executors

private const val MAX_CONCURRENT_ITEMS = 5

/**
 * Serves the sole purpose of adding items to queue and downloads
 * and then asks image compressor to compress the images
 */
class ImageRetriever(private val downloader: BufferedImageDownloader,
    private val imageProcessor: ImageProcessor) {

  private val priorityList = Stack<Pair<String, Disposable?>>()
  private val queuedItems = Stack<Pair<String, Disposable?>>()
  private val compositeDisposable = CompositeDisposable()

  fun retrieveFor(imageUri: Uri,
      dimens: Pair<Int, Int>) {
    if (hasProcessed(imageUri)) {
      return
    }
    imageProcessor.updateDimensFor(imageUri.toString(), dimens)
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

  @SuppressLint("CheckResult")
  private fun addToJobs(
      currentJobItem: Pair<String, Disposable?>) {
    if (priorityList.size < MAX_CONCURRENT_ITEMS) {
      // and move it to priority list if priorityList size < 5
      priorityList.add(currentJobItem)
      compositeDisposable += downloader.downloadFileByOkio(currentJobItem.first)
          .countedThreadScheduler(MAX_CONCURRENT_ITEMS)
          .doOnSubscribe {
            currentJobItem.second = it
          }
          .subscribe({ progress ->
            imageProcessor.updateProgress(currentJobItem.first, progress)
          }, {
            Log.e("GrabIV", "errrrr for ${currentJobItem.first}")
            imageProcessor.throwError(currentJobItem.first)
          }, {
            priorityList.remove(currentJobItem)
            processPriorityList()
            currentJobItem.first.let {
              putEntry(it)
            }
          })
    } else {
      queuedItems.add(currentJobItem)
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
    imageProcessor.notifyDownloaded(url)
  }

  fun removeListener(imageUri: Uri?) {
    imageUri?.let {
      imageProcessor.clearListeners(imageUri.toString())
    }
  }

  fun addListener(it: Uri,
      onImageAvailableListener: OnImageAvailableListener) {
    imageProcessor.addListener(it.toString(), onImageAvailableListener)
  }

  fun canAddRequest(uri: Uri): Boolean {
    return !isProcessing(uri) && !hasProcessed(uri)
  }

  fun hasProcessed(uri: Uri): Boolean {
    return imageProcessor.checkProcessed(uri)
  }

  private fun isProcessing(uri: Uri): Boolean {
    return pendingRequestsContains(uri)
  }

  fun destroyRetriever() {
    imageProcessor.destroyImageProcessor()

    compositeDisposable.let {
      if (!it.isDisposed) {
        it.dispose()
      }
    }
  }
}

data class Pair<A, B>(
    var first: A,
    var second: B
)

private fun <T> Observable<T>.countedThreadScheduler(numthreads: Int): Observable<T> {
  return this.subscribeOn(Schedulers.from(Executors.newFixedThreadPool(numthreads)))
}
