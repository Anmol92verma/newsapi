package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import okhttp3.OkHttpClient
import java.io.File
import java.lang.ref.SoftReference

class GrabImageView : AppCompatImageView, OnImageAvailableListener {

  private var imageUri: Uri? = null
  private var softReference: SoftReference<OkHttpClient>? = null
  private var progressBarReference: SoftReference<ProgressBar>? = null

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr)


  override fun setImageURI(uri: Uri?) {
    // in case recyclerview is reusing the view
    post {
      uri?.let {
        tag = uri
        ImageRetriever.removeListener(this.imageUri)
        this.imageUri = it
        setImageBitmap(null)
        ImageRetriever.addListener(this, it)
        this.imageUri?.let {
          ImageRetriever.getEntry(uri.toString())?.let {
            super.setImageURI(Uri.fromFile(it))
          } ?: kotlin.run {
            progressBarReference?.get()?.progress = 0
            this.imageUri?.let {
              ImageRetriever.retrieveFor(it)
            }
          }
        } ?: kotlin.run {
          Log.e("GrabImagheView", "Got null image url")
        }
      }

    }
  }

  override fun onImageProgress(forUrl: Uri, progress: Int) {
    post {
      this.imageUri?.let {
        if (it.toString().equals(forUrl.toString()) && ImageRetriever.getEntry(
                forUrl.toString()) == null) {
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

  override fun onImageAvailable(forUrl: Uri, bitmap: File) {
    post {
      this.imageUri?.let {
        if (it.toString() == forUrl.toString()) {
          super.setImageURI(Uri.fromFile(bitmap))
        }
      }
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    ImageRetriever.removeFromAllqueues(this.imageUri)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    imageUri?.let {
      setImageURI(it)
    }
  }

  fun setHttpClient(provideOkHttpClient: OkHttpClient) {
    this.softReference = SoftReference(provideOkHttpClient)
  }


  override fun getCacheDir(): File {
    return context.cacheDir
  }

  override fun getDimens(): Pair<Int, Int> {
    return Pair(measuredWidth, measuredHeight)
  }

  override fun onImageError(parse: Uri?) {
    post {
      parse?.let {
        if (it.toString().equals(this.imageUri.toString())) {
          val file = ImageRetriever.getCacheFile(it.toString(), getCacheDir())
          Log.e("Cache file", "${file.length()} ${file.absolutePath}")
          super.setImageURI(Uri.fromFile(file))
        }
      }
    }
  }

  override fun getHttpClient(): OkHttpClient? {
    softReference?.let {
      it.get()?.let { okHttpClient ->
        return okHttpClient
      }
    }
    return null
  }

  fun setProgressView(progressBar: ProgressBar) {
    this.progressBarReference = SoftReference(progressBar)
  }
}