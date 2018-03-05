package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.models.FullTournament
import kotterknife.bindView

class TournamentInfoLayout : LifecycleScrollView, TournamentPagerAdapterView {

    private val openLink: Button by bindView(R.id.bOpenLink)
    private val share: Button by bindView(R.id.bShare)
    private val entrants: TextView by bindView(R.id.tvEntrants)
    private val name: TextView by bindView(R.id.tvName)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
                @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    companion object {
        fun inflate(parent: ViewGroup): TournamentInfoLayout = LayoutInflater.from(
                parent.context).inflate(R.layout.layout_tournament_info, parent,
                false) as TournamentInfoLayout
    }

    override fun setContent(content: FullTournament) {

    }

}
