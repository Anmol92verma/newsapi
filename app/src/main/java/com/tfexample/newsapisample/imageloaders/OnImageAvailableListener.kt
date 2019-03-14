package com.tfexample.newsapisample.imageloaders

import android.content.Context
import android.net.Uri
import okhttp3.OkHttpClient
import java.io.File

interface OnImageAvailableListener {
  fun onImageAvailable(forUrl: Uri, bitmap: File)
  fun onImageProgress(forUrl: Uri, progress: Int)
  fun getCacheDir(): File
  fun getHttpClient(): OkHttpClient?
  fun getContext(): Context
  fun onImageError(parse: Uri?)
  fun getDimens(): Pair<Int, Int>
}