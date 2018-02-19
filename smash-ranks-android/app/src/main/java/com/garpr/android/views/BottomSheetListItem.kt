package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import kotterknife.bindView

class BottomSheetListItem : LinearLayout, View.OnClickListener {

    private val icon: ImageView by bindView(R.id.icon)
    private val text: TextView by bindView(R.id.text)


    companion object {
        fun inflate(parent: ViewGroup): BottomSheetListItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottom_sheet_list_item, parent, false) as BottomSheetListItem
    }

    interface OnClickListener {
        fun onClick(v: BottomSheetListItem)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun clickParentBottomSheetListLayout(view: View?) {
        if (view is OnClickListener) {
            (view as OnClickListener).onClick(this)
        } else if (view != null) {
            clickParentBottomSheetListLayout(view.parent as View)
        }
    }

    override fun onClick(v: View) {
        clickParentBottomSheetListLayout(parent as View)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnClickListener(this)
    }

    fun set(@DrawableRes icon: Int = 0, text: CharSequence) {
        this.icon.setImageResource(icon)
        this.text.text = text
    }

}
