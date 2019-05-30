package com.tfexample.newsapisample.networking.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.text.format.DateUtils
import com.tfexample.newsapisample.data.ARTICLES_TABLE
import com.tfexample.newsapisample.data.SOURCES_TABLE
import java.text.ParseException
import java.text.SimpleDateFormat


data class NewsListingModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

@Entity(tableName = SOURCES_TABLE)
data class Source(
    val id: String?,
    @PrimaryKey
    val name: String
) : NewsAdapterItem()

open class NewsAdapterItem

data class Article(
    var author: String?,
    var content: String?,
    var description: String?,
    var publishedAt: String,
    var source: Source?,
    var title: String?,
    var url: String?,
    var urlToImage: String?
)

// the fields are optionals for a reason, that room throws a non null exception
@Entity(tableName = ARTICLES_TABLE)
data class DBArticle(
    var author: String?,
    var content: String?,
    var description: String?,
    @PrimaryKey
    var publishedAt: String,
    @ForeignKey(entity = Source::class, parentColumns = arrayOf("name"),
        childColumns = arrayOf("source"))
    var source: String?,
    var title: String?,
    var url: String?,
    var urlToImage: String?,
    var isFav:Boolean
) : NewsAdapterItem() {
  fun publishedAtTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return try {
      val date = sdf.parse(publishedAt)
      DateUtils.getRelativeTimeSpanString(
          date.time, System.currentTimeMillis(),
          0L, DateUtils.FORMAT_ABBREV_ALL
      ).toString()
    } catch (e: ParseException) {
      e.printStackTrace()
      publishedAt ?: ""
    }
  }

}

