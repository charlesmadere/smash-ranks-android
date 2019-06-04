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
import com.garpr.android.extensions.appComponent
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.IdentityRepository
import javax.inject.Inject

abstract class IdentityFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleFrameLayout(context, attrs), IdentityRepository.OnIdentityChangeListener, Refreshable {

    protected var identity: AbsPlayer? = null
    private val originalBackground: Drawable? = background
    protected var identityId: String? = null

    @Inject
    protected lateinit var identityRepository: IdentityRepository


    init {
        if (!isInEditMode) {
            @Suppress("LeakingThis")
            appComponent.inject(this)
        }
    }

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
        super.onDetachedFromWindow()

        identityRepository.removeListener(this)
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
