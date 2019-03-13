package com.tfexample.newsapisample

import android.net.Uri
import okhttp3.OkHttpClient
import java.io.File

interface OnImageAvailableListener{
  fun onImageAvailable(forUrl: Uri)
  fun getCacheDir(): File
  fun getHttpClient(): OkHttpClient?
}