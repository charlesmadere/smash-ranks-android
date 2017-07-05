package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.TournamentActivity
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.misc.MiscUtils
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.AbsTournament
import kotterknife.bindView
import javax.inject.Inject

class TournamentDividerView : FrameLayout, BaseAdapterView<AbsTournament>, View.OnClickListener {

    var mContent: AbsTournament? = null
        private set


    @Inject
    lateinit protected var mRegionManager: RegionManager

    private val mDate: TextView by bindView(R.id.tvDate)
    private val mName: TextView by bindView(R.id.tvName)


    interface OnClickListener {
        fun onClick(v: TournamentDividerView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onClick(v: View) {
        val content = mContent ?: return
        val activity = MiscUtils.optActivity(context)

        if (activity is OnClickListener) {
            activity.onClick(this)
        } else {
            context.startActivity(TournamentActivity.getLaunchIntent(context, content,
                    mRegionManager.getRegion(context)))
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.sInstance.mAppComponent.inject(this)
        setOnClickListener(this)
    }

    override fun setContent(content: AbsTournament) {
        mContent = content

        mName.text = content.name
        mDate.text = content.date.mediumForm
    }

}
