package com.garpr.android.features.common.fragments

import androidx.fragment.app.Fragment
import com.garpr.android.misc.Heartbeat

abstract class BaseFragment : Fragment(), Heartbeat {

    override val isAlive: Boolean
        get() = isAdded && !isRemoving && !isDetached

}
