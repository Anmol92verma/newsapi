package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.tfexample.newsapisample.injection.ApplicationContext
import java.io.File

/**
 * Communicates with the image retriever
 */
class GrabImageLoader(@ApplicationContext val context: Context,
    private val imageRetriever: ImageRetriever) {

  companion object {
    fun getDownloadDir(forUrl: String, cacheDir: File): File {
      val fileNameWithExtension = URLUtil.guessFileName(forUrl, null, null)
      return File(getDownloadedCached(cacheDir), fileNameWithExtension)
    }

    fun getDestinationCachedFile(forUrl: String, cacheDir: File?): File {
      val fileNameWithExtension = URLUtil.guessFileName(forUrl, null, null)
      return File(getDestinationCached(cacheDir), fileNameWithExtension)
    }

    fun getDestinationCached(cacheDir: File?): String? {
      val file = File(cacheDir, "compressed")
      file.mkdirs()
      return file.absolutePath
    }

    private fun getDownloadedCached(cacheDir: File?): String? {
      val file = File(cacheDir, "downloads")
      file.mkdirs()
      return file.absolutePath
    }
  }

  fun loadUrl(uri: Uri,
      onImageAvailableListener: OnImageAvailableListener,
      dimens: Pair<Int, Int>) {
    if (imageRetriever.canAddRequest(uri)) {
      imageRetriever.addListener(uri, onImageAvailableListener)
      imageRetriever.retrieveFor(uri, dimens)
    }

    if (imageRetriever.hasProcessed(uri)) {
      onImageAvailableListener.onImageAvailable(uri)
    }
  }

  fun removeRequest(imageUri: Uri?) {
    this.imageRetriever.removeListener(imageUri)
    this.imageRetriever.removeFromAllqueues(imageUri)
  }

  fun getCacheFile(it: Uri): File? {
    return GrabImageLoader.getDestinationCachedFile(it.toString(), context.cacheDir)
  }

  fun destroyImageLoader() {
    imageRetriever.destroyRetriever()
  }

}