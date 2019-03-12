package com.tfexample.newsapisample.ui.news

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat

data class NewsListingModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
) {
  fun publishedAtTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return try {
      val date = sdf.parse(publishedAt)
      DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(),
          0L, DateUtils.FORMAT_ABBREV_ALL).toString()
    } catch (e: ParseException) {
      e.printStackTrace()
      publishedAt
    }
  }
}

data class Source(
    val id: String,
    val name: String
)