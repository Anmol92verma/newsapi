package com.tfexample.newsapisample.ui.news

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.support.annotation.NonNull
import android.text.format.DateUtils
import com.google.gson.Gson
import com.tfexample.newsapisample.dataproviders.ARTICLES_TABLE
import java.text.ParseException
import java.text.SimpleDateFormat


data class NewsListingModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)

// the fields are optionals for a reason, that room throws a non null exception
@Entity(tableName = ARTICLES_TABLE)
data class Article(
    var author: String?,
    var content: String?,
    var description: String?,
    @PrimaryKey
    var publishedAt: String,
    @TypeConverters(SourceTypeConverters::class)
    var source: Source?,
    var title: String?,
    var url: String?,
    var urlToImage: String?
) {
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

class SourceTypeConverters {
    @TypeConverter
    fun storedStringToSource(value: String): Source {
        return Gson().fromJson(value, Source::class.java)
    }

    @TypeConverter
    fun sourceToStoredString(cl: Source): String {
        return Gson().toJson(cl)
    }
}

data class Source(
    val id: String?,
    val name: String?
)