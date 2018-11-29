package com.garpr.android.dialogs

import com.garpr.android.R
import com.garpr.android.misc.Heartbeat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), Heartbeat {

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override val isAlive: Boolean
        get() = isAdded && !isRemoving && !isDetached

}
