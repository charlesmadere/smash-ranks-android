package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import kotterknife.bindView

class BottomSheetListLayout : LinearLayout, BottomSheetListItem.OnClickListener {

    private lateinit var titleText: CharSequence
    private lateinit var listItems: List<ListItem>
    private lateinit var onItemSelectListener: OnItemSelectListener

    private val list: LinearLayout by bindView(R.id.list)
    private val title: TextView by bindView(R.id.title)


    companion object {
        fun inflateForDialog(context: Context, title: CharSequence, listItems: List<ListItem>,
                onItemSelectListener: OnItemSelectListener): BottomSheetListLayout {
            val layout = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_list_layout,
                    null, false) as BottomSheetListLayout
            layout.titleText = title
            layout.listItems = listItems
            layout.onItemSelectListener = onItemSelectListener
            return layout
        }
    }

    interface OnItemSelectListener {
        fun onItemSelect(which: Int)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        title.text = titleText

        if (listItems.isEmpty()) {
            throw RuntimeException("listItems must not be empty")
        }

        listItems.forEach {
            val listItem = BottomSheetListItem.inflate(this)
            listItem.set(it.icon, it.text)
            list.addView(listItem)
        }
    }

    override fun onClick(v: BottomSheetListItem) {
        for (i in 0 until list.childCount) {
            if (list.getChildAt(i) === v) {
                onItemSelectListener.onItemSelect(i)
                return
            }
        }

        throw RuntimeException("onClick() occurred with unknown \"which\" value...")
    }

    class ListItem(@DrawableRes val icon: Int = 0, val text: CharSequence)

}
