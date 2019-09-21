package com.garpr.android.features.common.views

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.garpr.android.R
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.IdentityRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class IdentityConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleConstraintLayout(context, attrs), IdentityRepository.OnIdentityChangeListener,
        KoinComponent, Refreshable {

    protected var identity: AbsPlayer? = null
    private var originalBackground: Drawable? = null
    protected var identityId: String? = null

    protected val identityRepository: IdentityRepository by inject()

    protected open fun clear() {
        identity = null
        identityId = null
        refresh()
    }

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

        identityRepository.addListener(this)
    }

    override fun onDetachedFromWindow() {
        identityRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            identityRepository.addListener(this)
        }

        originalBackground = background
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        if (identityRepository.isPlayer(identity) || identityRepository.isPlayer(identityId)) {
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
