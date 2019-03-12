package com.tfexample.anmolgrabassignment.ui

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tfexample.anmolgrabassignment.injection.DaggerComponentManager
import com.tfexample.anmolgrabassignment.injection.InjectableViewModelFactory
import com.tfexample.anmolgrabassignment.injection.components.ActivityComponent
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding, T : ViewModel> : AppCompatActivity() {
    @Inject
    protected lateinit var viewModelFactory: InjectableViewModelFactory<T>
    protected lateinit var binding: B
    protected lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onComponentCreated(DaggerComponentManager.getActivityComponent(this))
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        val classVm = getViewModelClass()
        viewModel = viewModelFactory.create(classVm)
    }

    abstract fun getViewModelClass(): Class<T>
    abstract fun getLayoutId(): Int
    protected abstract fun onComponentCreated(component: ActivityComponent)
}