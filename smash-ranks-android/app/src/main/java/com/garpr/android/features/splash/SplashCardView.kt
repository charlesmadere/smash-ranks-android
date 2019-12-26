package com.garpr.android.features.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.getLong
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.DeviceUtils
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_splash.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class SplashCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : MaterialCardView(context, attrs), DialogInterface.OnClickListener, KoinComponent {

    private var hasAnimated: Boolean = false
    var listeners: Listeners? = null

    private val customizeIdentityClickListener = OnClickListener {
        listeners?.onCustomizeIdentityClick(this)
    }

    private val customizeRegionClickListener = OnClickListener {
        listeners?.onCustomizeRegionClick(this)
    }

    private val deleteIdentityClickListener = OnClickListener {
        showDeleteIdentityConfirmationDialog()
    }

    private val startUsingTheAppClickListener = OnClickListener {
        listeners?.onStartUsingTheAppClick(this)
    }

    protected val deviceUtils: DeviceUtils by inject()

    interface Listeners {
        fun onCustomizeIdentityClick(v: SplashCardView)
        fun onCustomizeRegionClick(v: SplashCardView)
        fun onRemoveIdentityClick(v: SplashCardView)
        fun onStartUsingTheAppClick(v: SplashCardView)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        listeners?.onRemoveIdentityClick(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        customizeRegion.setOnClickListener(customizeRegionClickListener)
        startUsingTheApp.setOnClickListener(startUsingTheAppClickListener)
    }

    private fun performAnimation() {
        animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(animationDuration)
                .setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR)
                .setListener(animationListener)
                .setStartDelay(animationDelay)
                .start()
    }

    fun setContent(identity: FavoritePlayer?, region: Region) {
        if (identity == null) {
            customizeIdentity.descriptionText = context.getText(
                    R.string.customize_identity_description)
            customizeIdentity.titleText = context.getText(R.string.customize_identity)
            customizeIdentity.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_chevron_right_white_24dp))
            customizeIdentity.setOnClickListener(customizeIdentityClickListener)
        } else {
            customizeIdentity.descriptionText = context.getString(R.string.identity_region_format,
                    identity.name, identity.region.displayName)
            customizeIdentity.titleText = context.getText(R.string.delete_identity)
            customizeIdentity.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.ic_delete_white_24dp))
            customizeIdentity.setOnClickListener(deleteIdentityClickListener)
        }

        customizeRegion.descriptionText = context.getString(R.string.region_endpoint_format,
                region.displayName, context.getString(region.endpoint.title))

        if (!hasAnimated) {
            hasAnimated = true

            if (deviceUtils.hasLowRam) {
                visibility = VISIBLE
            } else {
                performAnimation()
            }
        }
    }

    private fun showDeleteIdentityConfirmationDialog() {
        AlertDialog.Builder(context)
                .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show()
    }

    ///////////////////////////////
    // BEGIN ANIMATION VARIABLES //
    ///////////////////////////////

    private val animationListener: Animator.AnimatorListener by lazy {
        object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                alpha = 0f
                scaleX = 0.5f
                scaleY = 0.5f
                visibility = VISIBLE
            }
        }
    }

    private val animationDelay: Long by lazy {
        resources.getLong(R.integer.splash_card_animation_delay)
    }

    private val animationDuration: Long by lazy {
        resources.getLong(R.integer.splash_card_animation_duration)
    }

    /////////////////////////////
    // END ANIMATION VARIABLES //
    /////////////////////////////

}
