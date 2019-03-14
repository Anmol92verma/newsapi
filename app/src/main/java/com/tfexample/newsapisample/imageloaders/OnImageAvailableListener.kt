package com.tfexample.newsapisample.imageloaders

import android.net.Uri
import java.io.File

interface OnImageAvailableListener {
  fun onImageAvailable(forUrl: Uri)
  fun onImageProgress(forUrl: Uri, progress: Int)
  fun onImageError(parse: Uri?)
  fun getDimens(): Pair<Int, Int>
}