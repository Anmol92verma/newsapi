package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import com.tfexample.newsapisample.injection.DaggerComponentManager
import java.lang.ref.SoftReference
import javax.inject.Inject

class GrabImageView : AppCompatImageView, OnImageAvailableListener {

  @Inject
  lateinit var grabImageLoader: GrabImageLoader
  private var imageUri: Uri? = null
  private var progressBarReference: SoftReference<ProgressBar>? = null

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
      uri?.let {
        this.imageUri = it
        setImageBitmap(null)
        progressBarReference?.get()?.progress = 0
        grabImageLoader.loadUrl(it, this,getDimens())
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
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBarReference?.get()?.setProgress(progress, true)
          } else {
            progressBarReference?.get()?.progress = progress
          }
        }
      }
    }
  }

  override fun onImageAvailable(forUrl: Uri) {
    post {
      this.imageUri?.let {
        if (it.toString() == forUrl.toString()) {
          val cacheFile = GrabImageLoader.getCacheFile(forUrl.toString(), context.cacheDir)
          super.setImageURI(Uri.fromFile(cacheFile))
        }
      }
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    grabImageLoader.removeRequest(this.imageUri)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    imageUri?.let {
      setImageURI(it)
    }
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

  fun setProgressView(progressBar: ProgressBar) {
    this.progressBarReference = SoftReference(progressBar)
  }
}