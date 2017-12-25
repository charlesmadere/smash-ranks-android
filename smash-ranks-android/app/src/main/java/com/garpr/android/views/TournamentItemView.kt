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
import com.garpr.android.extensions.clear
import com.garpr.android.misc.RegionManager
import com.garpr.android.models.AbsTournament
import kotterknife.bindView
import javax.inject.Inject

class TournamentItemView : FrameLayout, BaseAdapterView<AbsTournament>, View.OnClickListener {

    @Inject
    protected lateinit var regionManager: RegionManager

    private val date: TextView by bindView(R.id.tvDate)
    private val name: TextView by bindView(R.id.tvName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun clear() {
        date.clear()
        name.clear()
    }

    override fun onClick(v: View) {
        val tournament = this.tournament ?: return
        context.startActivity(TournamentActivity.getLaunchIntent(context, tournament,
                regionManager.getRegion(context)))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
    }

    override fun setContent(content: AbsTournament) {
        tournament = content
    }

    private var tournament: AbsTournament? = null
        set(value) {
            field = value

            if (value == null) {
                clear()
                return
            }

            name.text = value.name
            date.text = value.date.shortForm
        }

}
