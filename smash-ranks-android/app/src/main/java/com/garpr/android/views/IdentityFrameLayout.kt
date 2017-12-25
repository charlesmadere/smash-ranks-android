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

abstract class IdentityFrameLayout : LifecycleFrameLayout,
        IdentityManager.OnIdentityChangeListener {

    private var originalBackground: Drawable? = null

    @Inject
    protected lateinit var identityManager: IdentityManager


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    protected open fun clear() {
        identity = null
        identityId = null
    }

    protected var identity: AbsPlayer? = null

    protected var identityId: String? = null

    protected open fun identityIsSomeoneElse() {
        ViewCompat.setBackground(this, originalBackground)
    }

    protected open fun identityIsUser() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.card_background))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        identityManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
            identityManager.addListener(this)
        }

        originalBackground = background
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refreshIdentity()
        }
    }

    protected open fun refreshIdentity() {
        if (identityManager.isPlayer(identity) || identityManager.isPlayer(identityId)) {
            identityIsUser()
        } else {
            identityIsSomeoneElse()
        }
    }

    protected fun styleTextViewForSomeoneElse(view: TextView) {
        view.typeface = Typeface.DEFAULT
    }

    protected fun styleTextViewForUser(view: TextView) {
        view.typeface = Typeface.DEFAULT_BOLD
    }

}
