package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
import com.tfexample.newsapisample.injection.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrabImageLoader @Inject constructor(@ApplicationContext val context: Context,
    private val imageRetriever: ImageRetriever) {

  companion object {
    fun getCacheFile(forUrl: String, cacheDir: File): File {
      val fileNameWithExtension = URLUtil.guessFileName(forUrl, null, null);
      return File(cacheDir, fileNameWithExtension)
    }
  }

  fun loadUrl(uri: Uri,
      onImageAvailableListener: OnImageAvailableListener,
      dimens: Pair<Int, Int>) {
    if (imageRetriever.canAddRequest(uri)) {
      imageRetriever.addListener(onImageAvailableListener, uri)
      imageRetriever.retrieveFor(uri,dimens)
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
    return GrabImageLoader.getCacheFile(it.toString(), context.cacheDir)
  }

}