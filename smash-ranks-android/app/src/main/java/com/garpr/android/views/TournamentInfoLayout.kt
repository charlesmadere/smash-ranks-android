package com.garpr.android.views

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.models.FullTournament

class TournamentInfoLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
) : LifecycleConstraintLayout(context, attrs, defStyleAttr), TournamentPagerAdapterView {




    companion object {
        fun inflate(parent: ViewGroup): TournamentInfoLayout = LayoutInflater.from(
                parent.context).inflate(R.layout.layout_tournament_info, parent,
                false) as TournamentInfoLayout
    }

    override fun setContent(content: FullTournament) {

    }

}
