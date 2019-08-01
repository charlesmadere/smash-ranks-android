package com.garpr.android.features.splash

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.garpr.android.R
import com.garpr.android.extensions.activity
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.getLong
import com.garpr.android.features.common.views.LifecycleCardView
import com.garpr.android.features.setIdentity.SetIdentityActivity
import com.garpr.android.features.setRegion.SetRegionActivity
import com.garpr.android.misc.AnimationUtils
import com.garpr.android.misc.DeviceUtils
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.IdentityRepository
import com.garpr.android.repositories.RegionRepository
import kotlinx.android.synthetic.main.activity_splash.view.*
import javax.inject.Inject

class SplashCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LifecycleCardView(context, attrs), DialogInterface.OnClickListener,
        IdentityRepository.OnIdentityChangeListener, Refreshable,
        RegionRepository.OnRegionChangeListener {

    private var hasAnimated: Boolean = false

    @Inject
    protected lateinit var deviceUtils: DeviceUtils

    @Inject
    protected lateinit var identityRepository: IdentityRepository

    @Inject
    protected lateinit var regionRepository: RegionRepository


    interface Listener {
        fun onStartUsingTheAppClick(v: SplashCardView)
    }

    init {
        if (!isInEditMode) {
            appComponent.inject(this)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        identityRepository.addListener(this)
        regionRepository.addListener(this)
        refresh()

        if (!hasAnimated && !deviceUtils.hasLowRam) {
            hasAnimated = true
            performFullAnimation()
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        identityRepository.removeIdentity()
    }

    override fun onDetachedFromWindow() {
        identityRepository.removeListener(this)
        regionRepository.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        customizeIdentity.setOnClickListener {
            if (identityRepository.hasIdentity) {
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

        identityRepository.addListener(this)
        regionRepository.addListener(this)
        refresh()
    }

    override fun onIdentityChange(identityRepository: IdentityRepository) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onRegionChange(regionRepository: RegionRepository) {
        if (isAlive) {
            refresh()
        }
    }

    private fun performFullAnimation() {
        alpha = 0f
        scaleX = 0.5f
        scaleY = 0.5f

        animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(resources.getLong(R.integer.splash_card_animation_duration))
                .setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR)
                .setStartDelay(resources.getLong(R.integer.splash_card_animation_delay))
                .start()
    }

    override fun refresh() {
        val identity = identityRepository.identity

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

        val region = regionRepository.getRegion(context)
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
