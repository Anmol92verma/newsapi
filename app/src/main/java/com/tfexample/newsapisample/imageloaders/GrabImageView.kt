package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import com.tfexample.newsapisample.injection.DaggerComponentManager
import javax.inject.Inject

class GrabImageView : AppCompatImageView, OnImageAvailableListener {

  @Inject
  lateinit var grabImageLoader: GrabImageLoader
  private var imageUri: Uri? = null

  constructor(context: Context?) : super(context) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr) {
    init()
  }

  private fun init() {
    DaggerComponentManager.appComponent?.inject(this)
  }

  override fun setImageURI(uri: Uri?) {
    // in case recyclerview is reusing the view
    post {
      setImageBitmap(null)
      uri?.let {
        this.imageUri = it
        grabImageLoader.loadUrl(it, this, getDimens())
      } ?: kotlin.run {
        Log.e("GrabImagheView", "Got null image url")
      }

    }
  }

  override fun onImageProgress(forUrl: Uri, progress: Int) {
    post {
      this.imageUri?.let {
        if (it.toString().equals(forUrl.toString())) {
          Log.d("prgresss", "$forUrl >> $progress")
        }
      }
    }
  }

  override fun onImageAvailable(forUrl: Uri) {
    post {
      this.imageUri?.let {
        if (it.toString() == forUrl.toString()) {
          val cacheFile = GrabImageLoader.getDestinationCachedFile(forUrl.toString(),
              context.cacheDir)
          super.setImageURI(Uri.fromFile(cacheFile))
        }
      }
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    grabImageLoader.removeRequest(this.imageUri)
  }

  override fun getDimens(): Pair<Int, Int> {
    return Pair(measuredWidth, measuredHeight)
  }

  override fun onImageError(parse: Uri?) {
    post {
      parse?.let {
        if (it.toString().equals(this.imageUri.toString())) {
          val file = grabImageLoader.getCacheFile(it)
          file?.let {
            Log.e("Cache file", "${file.length()} ${file.absolutePath}")
            super.setImageURI(Uri.fromFile(file))
          }
        }
      }
    }
  }
}