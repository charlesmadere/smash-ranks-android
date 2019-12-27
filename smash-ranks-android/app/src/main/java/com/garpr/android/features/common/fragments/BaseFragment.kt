package com.garpr.android.features.common.fragments

import androidx.fragment.app.Fragment
import com.garpr.android.misc.Heartbeat
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment(), Heartbeat {

    override val isAlive: Boolean
        get() = isAdded && !isRemoving && !isDetached

    protected val onCreateViewDisposable = CompositeDisposable()

    override fun onDestroyView() {
        onCreateViewDisposable.dispose()
        super.onDestroyView()
    }

}
