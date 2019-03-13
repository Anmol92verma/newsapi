package com.tfexample.newsapisample

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.tfexample.newsapisample.ImageCacheBuilder.getCacheFile
import okhttp3.OkHttpClient
import java.io.File
import java.lang.ref.SoftReference

class GrabImageView : AppCompatImageView, OnImageAvailableListener {
  override fun getCacheDir(): File {
    return context.cacheDir
  }

  override fun getHttpClient(): OkHttpClient? {
    softReference?.let {
      it.get()?.let { okHttpClient ->
        return okHttpClient
      }
    }
    return null
  }

  private var softReference: SoftReference<OkHttpClient>? = null

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr)

  private var imageUri: Uri? = null

  override fun setImageURI(uri: Uri?) {
    // in case recyclerview is reusing the view
    if (imageUri != null) {
      setImageBitmap(null)
    }
    this.imageUri = uri
    imageUri?.let { image ->
      ImageCacheBuilder.retrieveFor(image, this)
    }
  }

  override fun onImageAvailable(forUrl: Uri) {
    val file = getCacheFile(forUrl.toString(), cacheDir = context.cacheDir)
    super.setImageURI(Uri.fromFile(file))
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    ImageCacheBuilder.removeRequest(this.imageUri)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    imageUri?.let {
      softReference?.let {
        it.get()?.let {
          setImageURI(imageUri)
        }
      }
    }
  }

  fun setHttpClient(provideOkHttpClient: OkHttpClient) {
    this.softReference = SoftReference(provideOkHttpClient)
  }
}