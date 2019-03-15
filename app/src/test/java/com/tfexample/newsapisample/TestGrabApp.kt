package com.tfexample.newsapisample

class TestGrabApp : GrabApp() {

  override fun onCreate() {
    super.onCreate()
    TestDaggerComponentManager.initialize(this)
  }

}