package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.tfexample.newsapisample.ui.news.subsIoObsMain
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.Util
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio

class BufferedImageDownloader(private val okHttpClient: OkHttpClient,
    private val context: Context) {

  fun downloadFileByOkio(
      url: String,
      dimens: Pair<Int, Int>?
  ): Observable<Int> {
    return Observable.create<Int> { subscriber ->
      val destFile = GrabImageLoader.getCacheFile(url, context.cacheDir)
      var sink: BufferedSink? = null
      var source: BufferedSource? = null
      var gotException = false
      try {
        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val body = response.body()
        body?.let { it ->
          val contentLength = it.contentLength()
          if (destFile.length() == contentLength) {
            subscriber.safeOnNext(100)
            subscriber.safeOnComplete()
          } else {
            source = body.source()
            when {
              destFile.length() == 0L -> {
                sink = Okio.buffer(Okio.sink(destFile))
              }
              destFile.length() > 0L -> {
                sink = Okio.buffer(Okio.appendingSink(destFile))
              }
            }

            sink?.let {
              val sinkBuffer = it.buffer()
              var totalBytesRead: Long = 0
              val bufferSize = 8 * 1024
              var bytesRead: Long = 0

              if (destFile.length() > 0) {
                Log.d("Already read", "$bytesRead/$contentLength")
                bytesRead = destFile.length()
                try {
                  source?.skip(bytesRead)
                } catch (ex: java.lang.Exception) {

                }
              }

              fun readStatus(source: BufferedSource): Long {
                bytesRead = source.read(sinkBuffer, bufferSize.toLong())
                return bytesRead
              }


              while (readStatus(source!!) != -1L && !Thread.currentThread().isInterrupted) {
                it.emit()
                totalBytesRead += bytesRead
                val progress = (totalBytesRead * 100 / contentLength).toInt()
                subscriber.safeOnNext(progress)
              }

              it.flush()
            }
          }

        } ?: run {
          subscriber.safeOnError(Exception("no body exception"))
        }
      } catch (e: Exception) {
        gotException = true
        Log.d("ImageDownloaded", "Interrupted for $url")
        subscriber.safeOnError(e)
      } finally {
        Util.closeQuietly(sink)
        Util.closeQuietly(source)
        Compressor(context).setQuality(10).setCompressFormat(
            Bitmap.CompressFormat.WEBP).compressToFile(destFile)
      }
      if (!gotException) {
        subscriber.safeOnComplete()
      }
    }.subsIoObsMain()
  }

  private fun <T> ObservableEmitter<T>.safeOnComplete() {
    if (!this.isDisposed) {
      this.onComplete()
    }
  }

  private fun <T> ObservableEmitter<T>.safeOnError(e: Exception) {
    if (!this.isDisposed) {
      this.onError(e)
    }
  }

  private fun <T> ObservableEmitter<T>.safeOnNext(progress: T) {
    if (!this.isDisposed) {
      this.onNext(progress)
    }
  }


}