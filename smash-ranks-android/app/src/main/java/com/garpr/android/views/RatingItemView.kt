package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import com.garpr.android.adapters.BaseAdapterView
import com.garpr.android.models.Rating
import kotterknife.bindView

class RatingItemView : LinearLayout, BaseAdapterView<Rating> {

    private val mRating: TextView by bindView(R.id.tvRating)
    private val mUnadjusted: TextView by bindView(R.id.tvUnadjusted)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setContent(content: Rating) {
        mRating.text = resources.getString(R.string.rating_x, content.ratingTruncated)
        mUnadjusted.text = resources.getString(R.string.unadjusted_x_y, content.muTruncated,
                content.sigmaTruncated)
    }

}