package com.garpr.android.extensions

import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu

fun PopupMenu.addMenuItem(@StringRes titleRes: Int, listener: MenuItem.OnMenuItemClickListener): MenuItem {
    return menu.add(titleRes).setOnMenuItemClickListener(listener)
}
