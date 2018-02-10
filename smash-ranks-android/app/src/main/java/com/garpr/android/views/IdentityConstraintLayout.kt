package com.garpr.android.views

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.misc.IdentityManager
import com.garpr.android.models.AbsPlayer
import javax.inject.Inject

abstract class IdentityConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleConstraintLayout(context, attrs, defStyleAttr), IdentityManager.OnIdentityChangeListener {

    private var originalBackground: Drawable? = null

    @Inject
    protected lateinit var identityManager: IdentityManager


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

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)
        identityManager.addListener(this)

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
