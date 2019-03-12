package com.tfexample.newsapisample.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class InjectableViewModelFactory<VM> @Inject constructor(private val viewModel: Provider<VM>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModel.get() as T
    }
}