package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.IdentityManager
import com.garpr.android.models.AbsPlayer
import javax.inject.Inject

abstract class IdentityFrameLayout : LifecycleFrameLayout, IdentityManager.OnIdentityChangeListener {

    protected var mIdentity: AbsPlayer? = null
    protected var mIdentityId: String? = null
    private var mOriginalBackground: Drawable? = null

    @Inject
    protected lateinit var mIdentityManager: IdentityManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    protected open fun identityIsSomeoneElse() {
        ViewCompat.setBackground(this, mOriginalBackground)
    }

    protected open fun identityIsUser() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mIdentityManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mIdentityManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
        mIdentityManager.addListener(this)

        mOriginalBackground = background
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refreshIdentity()
        }
    }

    protected open fun refreshIdentity() {
        if (mIdentityManager.isPlayer(mIdentity)) {
            identityIsUser()
            return
        }

        if (mIdentityManager.isPlayer(mIdentityId)) {
            identityIsUser()
            return
        }

        identityIsSomeoneElse()
    }

    protected fun styleTextViewForSomeoneElse(view: TextView) {
        view.typeface = Typeface.DEFAULT
    }

    protected fun styleTextViewForUser(view: TextView) {
        view.typeface = Typeface.DEFAULT_BOLD
    }

}
