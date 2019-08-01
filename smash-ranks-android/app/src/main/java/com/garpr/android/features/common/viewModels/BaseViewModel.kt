package com.garpr.android.features.common.viewModels

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()


    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}
