package com.tfexample.newsapisample

import com.tfexample.newsapisample.ui.news.ActivityNewsListingGrab
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = [21])
class TestActivityNewsListingGrab {
  private var mainActivity: ActivityNewsListingGrab? = null

  @Before
  @Throws(Exception::class)
  fun setUp() {
    mainActivity = Robolectric.setupActivity(ActivityNewsListingGrab::class.java)
  }

  @Test
  @Throws(Exception::class)
  fun loadNewsListing() {
    assertEquals(1, mainActivity?.viewModel?.newsListing?.value?.size)
  }

}