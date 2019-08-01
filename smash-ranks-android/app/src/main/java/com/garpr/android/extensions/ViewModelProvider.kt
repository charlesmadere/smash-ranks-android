package com.garpr.android.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T) = T::class.java.let {
    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
    }).get(it)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T) = T::class.java.let {
    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) = factory() as T
    }).get(it)
}

inline fun <reified T : ViewModel> viewModel(activity: FragmentActivity, crossinline initializer: () -> T) =
        lazy(LazyThreadSafetyMode.NONE) { activity.getViewModel { initializer() } }

inline fun <reified T : ViewModel> viewModel(fragment: Fragment, crossinline initializer: () -> T) =
        lazy(LazyThreadSafetyMode.NONE) { fragment.getViewModel { initializer() } }
