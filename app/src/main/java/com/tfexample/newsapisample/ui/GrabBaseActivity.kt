package com.tfexample.newsapisample.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tfexample.newsapisample.injection.DaggerComponentManager
import com.tfexample.newsapisample.injection.ViewModelFactory
import com.tfexample.newsapisample.injection.components.ActivityComponent
import javax.inject.Inject

abstract class GrabBaseActivity<B : ViewDataBinding, T : ViewModel> : AppCompatActivity() {
  lateinit var binding: B
  lateinit var viewModel: T

  @Inject
  lateinit var viewmodeFactory: ViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    onComponentCreated(DaggerComponentManager.getActivityComponent(this))
    setContentView(getLayoutId())
    binding = DataBindingUtil.setContentView(this, getLayoutId())
    val classVm = getViewModelClass()
    viewModel = ViewModelProviders.of(this,viewmodeFactory)[classVm]
  }

  abstract fun getViewModelClass(): Class<T>
  abstract fun getLayoutId(): Int
  protected abstract fun onComponentCreated(component: ActivityComponent)
}