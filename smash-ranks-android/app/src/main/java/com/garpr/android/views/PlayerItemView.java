package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Player;

public class PlayerItemView extends LinearLayout implements BaseAdapterView<Player> {

    public PlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setContent(final Player content) {
        // TODO
    }

}
