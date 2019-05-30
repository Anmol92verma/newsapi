package com.tfexample.newsapisample.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.Source

@Database(entities = arrayOf(
    DBArticle::class, Source::class), version = 1)
abstract class Newsdatabase : RoomDatabase() {
  abstract fun articlesDao(): ArticlesDao
  abstract fun sourcesDao(): SourcesDao
}