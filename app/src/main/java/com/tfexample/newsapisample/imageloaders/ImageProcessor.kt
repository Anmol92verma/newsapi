package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.util.ArrayMap
import com.tfexample.newsapisample.injection.ApplicationContext
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class ImageProcessor(@ApplicationContext private val context: Context) {

  private val listeners = ArrayMap<String, OnImageAvailableListener>()
  private val compositeDisposable = CompositeDisposable()
  private val processedImageUrls = HashSet<String>()
  private val dimensMap = ArrayMap<String, Pair<Int, Int>>()

  fun updateProgress(first: String, progress: Int?) {
    progress?.let { listeners[first]?.onImageProgress(Uri.parse(first), it) }
  }

  fun throwError(parse: String) {
    listeners[parse]?.onImageError(Uri.parse(parse))
  }

  fun notifyDownloaded(url: String) {
    val destFile = GrabImageLoader.getDownloadDir(url, context.cacheDir)
    compositeDisposable += Compressor(context).setQuality(20)
        .setDestinationDirectoryPath(GrabImageLoader.getDestinationCached(context.cacheDir))
        .setCompressFormat(Bitmap.CompressFormat.JPEG)
        .setMaxWidth(dimensMap[url]!!.first)
        .setMaxHeight(dimensMap[url]!!.second)
        .compressToFileAsFlowable(destFile)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          processedImageUrls.add(url)
          listeners[url]?.onImageAvailable(Uri.parse(url))
        }, {
          it.printStackTrace()
        }, {

        })
  }

  fun clearListeners(url: String) {
    if (this.listeners.containsKey(url)) {
      this.listeners.remove(url)
    }
  }

  fun addListener(url: String,
      onImageAvailableListener: OnImageAvailableListener) {
    if (!this.listeners.containsKey(url)) {
      this.listeners[url] = onImageAvailableListener
    }
  }

  fun checkProcessed(uri: Uri): Boolean {
    return this.processedImageUrls.contains(uri.toString())
  }

  fun updateDimensFor(toString: String, dimens: Pair<Int, Int>) {
    this.dimensMap[toString] = dimens
  }

}