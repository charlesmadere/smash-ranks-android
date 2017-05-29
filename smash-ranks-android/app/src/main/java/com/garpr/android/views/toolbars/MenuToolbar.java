package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class MenuToolbar extends Toolbar {

    private boolean mMenuCreated;


    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected boolean isMenuCreated() {
        return mMenuCreated;
    }

    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        mMenuCreated = true;
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        return false;
    }

    public void refreshMenu() {

    }

}
