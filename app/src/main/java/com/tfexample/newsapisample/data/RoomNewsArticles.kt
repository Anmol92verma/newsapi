package com.tfexample.newsapisample.data

import android.arch.persistence.room.*
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source

const val NEWS_DATABASE = "NEWS_DATABASE"
const val ARTICLES_TABLE = "ARTICLES_TABLE"
const val SOURCES_TABLE = "SOURCES_TABLE"

@Dao
interface ArticlesDao {
  @Query("SELECT * FROM $ARTICLES_TABLE")
  fun getAllArticles(): List<DBArticle>

  @Query("SELECT * FROM $ARTICLES_TABLE WHERE publishedAt=:publishedAt")
  fun getArticleWith(publishedAt: String): DBArticle?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllArticles(articles: List<DBArticle>)

  @Query("SELECT * FROM ARTICLES_TABLE WHERE source=:id")
  fun getArticlesForSource(id: String): List<DBArticle>

  @Update
  fun update(copy: DBArticle)
}

@Dao
interface SourcesDao {
  @Query("SELECT * FROM $SOURCES_TABLE")
  fun getAllSources(): List<Source>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAllSources(articles: List<Source>)
}
