package com.tfexample.newsapisample.data

import android.arch.persistence.room.*
import android.content.Context
import com.tfexample.newsapisample.networking.models.Article
import com.tfexample.newsapisample.networking.models.SourceTypeConverters

const val NEWS_DATABASE = "NEWS_DATABASE"
const val ARTICLES_TABLE = "ARTICLES_TABLE"

@Dao
interface ArticlesDao {
  @Query("SELECT * FROM ARTICLES_TABLE")
  fun getAllArticles(): List<Article>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(articles: List<Article>)
}

@Database(entities = arrayOf(Article::class), version = 1)
@TypeConverters(value = [SourceTypeConverters::class])
abstract class Newsdatabase : RoomDatabase() {
  abstract fun articlesDao(): ArticlesDao
}

object NewsDatabaseProvider {
  private lateinit var database: Newsdatabase

  fun getNewsDatabase(applicationContext: Context): Newsdatabase {
    database = Room.databaseBuilder(
        applicationContext,
        Newsdatabase::class.java, NEWS_DATABASE
    ).fallbackToDestructiveMigration().build()
    return database
  }
}