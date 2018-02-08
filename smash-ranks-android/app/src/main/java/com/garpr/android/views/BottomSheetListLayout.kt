package com.garpr.android.views

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.design.widget.BottomSheetDialog
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.garpr.android.R
import kotterknife.bindView

class BottomSheetListLayout : LinearLayout, BottomSheetListItem.OnClickListener {

    private var onItemSelectListener: OnItemSelectListener? = null

    private val list: LinearLayout by bindView(R.id.list)
    private val title: TextView by bindView(R.id.title)


    companion object {
        fun createBottomSheetDialog(context: Context): Dialog {
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_list_layout,
                    null, false)

            val dialog = BottomSheetDialog(context)
            dialog.setContentView(view)
            return dialog
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

    override fun onClick(v: BottomSheetListItem) {
        onItemSelectListener?.let {
            for (i in 0 until list.childCount) {
                if (list.getChildAt(i) === v) {
                    it.onItemSelect(i)
                    return
                }
            }

            throw RuntimeException("onClick() occurred with unknown \"which\" value...")
        }
    }

    fun set(onItemSelectListener: OnItemSelectListener? = null, title: CharSequence,
            listItems: List<ListItem>) {
        this.onItemSelectListener = onItemSelectListener
        this.title.text = title

        list.removeAllViews()

        if (listItems.isEmpty()) {
            throw IllegalArgumentException("listItems must not be empty")
        }

        listItems.forEach({
            val listItem = BottomSheetListItem.inflate(this)
            listItem.set(it.icon, it.text)
            list.addView(listItem)
        })
    }

    class ListItem(@DrawableRes val icon: Int = 0, val text: CharSequence)

}
