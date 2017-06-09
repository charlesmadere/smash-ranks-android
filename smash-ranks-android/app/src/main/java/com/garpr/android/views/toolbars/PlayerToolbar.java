package com.garpr.android.views.toolbars;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;

import com.garpr.android.R;

public class PlayerToolbar extends SearchToolbar {

    public PlayerToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        inflater.inflate(R.menu.toolbar_player, menu);
        super.onCreateOptionsMenu(inflater, menu);
    }

    @Override
    public void onRefreshMenu() {
        super.onRefreshMenu();


    }

}
