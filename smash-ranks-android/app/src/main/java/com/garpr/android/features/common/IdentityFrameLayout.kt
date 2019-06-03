package com.garpr.android.features.common

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.managers.IdentityManager
import com.garpr.android.misc.Refreshable
import javax.inject.Inject

abstract class IdentityFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), IdentityManager.OnIdentityChangeListener, Refreshable {

    private var originalBackground: Drawable? = null

    @Inject
    protected lateinit var identityManager: IdentityManager


    protected open fun clear() {
        identity = null
        identityId = null
        refresh()
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
            appComponent.inject(this)
            identityManager.addListener(this)
        }

        originalBackground = background
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
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
