package com.tfexample.newsapisample.data

import android.arch.persistence.room.Room
import android.content.Context

object NewsDatabaseProvider {
  private lateinit var database: Newsdatabase

  fun getNewsDatabase(applicationContext: Context): Newsdatabase {
    database = Room.databaseBuilder(
        applicationContext,
        Newsdatabase::class.java,
        NEWS_DATABASE
    ).fallbackToDestructiveMigration().build()
    return database
  }
}