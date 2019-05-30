package com.tfexample.newsapisample.ui.news

import android.test.ActivityInstrumentationTestCase2
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class ActivityNewsListingGrabTest : ActivityInstrumentationTestCase2<ActivityNewsListingGrab>(ActivityNewsListingGrab::class.java) {

  @Rule
  @JvmField
  var mActivityTestRule = ActivityTestRule(ActivityNewsListingGrab::class.java)

  @Test
  fun activityNewsListingGrabTest() {

  }
}
