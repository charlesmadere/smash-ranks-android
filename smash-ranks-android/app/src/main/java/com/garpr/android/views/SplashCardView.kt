package com.garpr.android.views

import android.animation.ValueAnimator
import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.activities.SetIdentityActivity
import com.garpr.android.activities.SetRegionActivity
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.getLong
import com.garpr.android.managers.IdentityManager
import com.garpr.android.managers.RegionManager
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.activity_splash.view.*
import javax.inject.Inject

class SplashCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleCardView(context, attrs), DialogInterface.OnClickListener,
        IdentityManager.OnIdentityChangeListener, Refreshable,
        RegionManager.OnRegionChangeListener {

    private var hasAnimated: Boolean = false

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var identityManager: IdentityManager

    @Inject
    protected lateinit var regionManager: RegionManager


    interface Listener {
        fun onStartUsingTheAppClick(v: SplashCardView)
    }

    companion object {
        private const val ELEVATION_OVERSHOOT_TENSION: Float = 6f
        private const val MARGIN_OVERSHOOT_TENSION: Float = 12f
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityManager.addListener(this)
        regionManager.addListener(this)

        if (!hasAnimated && !deviceUtils.hasLowRam) {
            hasAnimated = true
            performFullAnimation()
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        identityManager.removeIdentity()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        identityManager.removeListener(this)
        regionManager.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        appComponent.inject(this)

        customizeIdentity.setOnClickListener {
            if (identityManager.hasIdentity) {
                showDeleteIdentityConfirmationDialog()
            } else {
                context.startActivity(SetIdentityActivity.getLaunchIntent(context))
            }
        }

        customizeRegion.setOnClickListener {
            context.startActivity(SetRegionActivity.getLaunchIntent(context))
        }

        startUsingTheApp.setOnClickListener {
            (activity as? Listener?)?.onStartUsingTheAppClick(this)
        }

        identityManager.addListener(this)
        regionManager.addListener(this)
        refresh()
    }

    override fun onIdentityChange(identityManager: IdentityManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onRegionChange(regionManager: RegionManager) {
        if (isAlive) {
            refresh()
        }
    }

    private fun performElevationAnimation() {
        val animator = ValueAnimator.ofFloat(cardElevation,
                resources.getDimension(R.dimen.splash_card_end_elevation))

        animator.addUpdateListener {
            cardElevation = it.animatedValue as Float
        }

        animator.duration = resources.getLong(R.integer.splash_card_animation_duration)
        animator.interpolator = OvershootInterpolator(ELEVATION_OVERSHOOT_TENSION)
        animator.start()
    }

    private fun performFullAnimation() {
        alpha = 0f
        scaleX = 0.6f
        scaleY = 0.6f

        animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(resources.getLong(R.integer.splash_card_animation_duration))
                .setInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR)
                .setStartDelay(resources.getLong(R.integer.splash_card_animation_delay))
                .withStartAction {
                    performElevationAnimation()
                    performMarginBottomAnimation()
                }
                .start()
    }

    private fun performMarginBottomAnimation() {
        val layoutParams = this.layoutParams as MarginLayoutParams

        val animator = ValueAnimator.ofInt(layoutParams.bottomMargin,
                resources.getDimensionPixelSize(R.dimen.root_padding_quadruple),
                resources.getDimensionPixelSize(R.dimen.root_padding_triple))

        animator.addUpdateListener {
            layoutParams.bottomMargin = it.animatedValue as Int
            this.layoutParams = layoutParams
        }

        animator.duration = resources.getLong(R.integer.splash_card_animation_duration)
        animator.interpolator = OvershootInterpolator(MARGIN_OVERSHOOT_TENSION)
        animator.start()
    }

    override fun refresh() {
        val identity = identityManager.identity

        if (identity == null) {
            customizeIdentity.descriptionText = context.getText(
                    R.string.customize_identity_description)
            customizeIdentity.titleText = context.getText(R.string.customize_identity)
            customizeIdentity.imageDrawable = ContextCompat.getDrawable(context,
                    R.drawable.ic_chevron_right_white_24dp)
        } else {
            customizeIdentity.descriptionText = context.getString(R.string.identity_region_format,
                    identity.name, identity.region.displayName)
            customizeIdentity.titleText = context.getText(R.string.delete_identity)
            customizeIdentity.imageDrawable = ContextCompat.getDrawable(context,
                    R.drawable.ic_delete_white_24dp)
        }

        val region = regionManager.getRegion(context)
        customizeRegion.descriptionText = context.getString(R.string.region_endpoint_format,
                region.displayName, context.getString(region.endpoint.title))
    }

    private fun showDeleteIdentityConfirmationDialog() {
        AlertDialog.Builder(context)
                .setMessage(R.string.are_you_sure_you_want_to_delete_your_identity)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes, this)
                .show()
    }

}
