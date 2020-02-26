package com.garpr.android.features.common.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import com.garpr.android.extensions.optHideKeyboard
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

    override fun startActivity(intent: Intent?) {
        activity?.optHideKeyboard()
        super.startActivity(intent)
    }

}
