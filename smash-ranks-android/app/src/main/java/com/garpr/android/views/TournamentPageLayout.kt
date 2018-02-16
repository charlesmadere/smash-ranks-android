package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.FullTournament
import kotterknife.bindView

abstract class TournamentPageLayout : SearchableFrameLayout, BaseAdapterView<FullTournament> {

    protected val empty: View by bindView(R.id.empty)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override val recyclerView: RecyclerView by bindView(R.id.recyclerView)

}
