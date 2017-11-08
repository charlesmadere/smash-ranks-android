package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import com.garpr.android.App
import com.garpr.android.misc.IdentityManager
import javax.inject.Inject

abstract class IdentityConstraintLayout : LifecycleConstraintLayout,
        IdentityManager.OnIdentityChangeListener {

    @Inject
    protected lateinit var mIdentityManager: IdentityManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)


    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
        mIdentityManager.addListener(this)
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refreshIdentity()
        }
    }

    protected open fun refreshIdentity() {
        // TODO
    }

}
