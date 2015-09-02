package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.misc.Heartbeat;
import com.garpr.android.models.Player;
import com.garpr.android.models.Region;
import com.garpr.android.settings.RegionSetting;
import com.garpr.android.settings.Settings;
import com.garpr.android.settings.Settings.User;


public class NavigationHeaderView extends RelativeLayout implements Heartbeat,
        RegionSetting.RegionListener {


    private static final String TAG = "NavigationHeaderView";

    private boolean mIsAlive;
    private TextView mPlayer;
    private TextView mRegion;




    public NavigationHeaderView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public NavigationHeaderView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationHeaderView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void findViews() {
        mPlayer = (TextView) findViewById(R.id.navigation_header_view_player);
        mRegion = (TextView) findViewById(R.id.navigation_header_view_region);
    }


    @Override
    public boolean isAlive() {
        return mIsAlive;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mIsAlive) {
            mIsAlive = true;
            updateRegion();
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        mIsAlive = false;
        Settings.Region.detachListener(this);
        super.onDetachedFromWindow();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIsAlive = true;
        findViews();
        updateRegion();
        updatePlayer();
    }


    @Override
    public void onRegionChanged(final Region region) {
        post(new Runnable() {
            @Override
            public void run() {
                updateRegion();
            }
        });
    }


    @Override
    public String toString() {
        return TAG;
    }


    private void updatePlayer() {
        if (User.hasPlayer()) {
            final Player player = User.Player.get();
            mPlayer.setText(player.getName());
            mPlayer.setVisibility(VISIBLE);
        } else {
            mPlayer.setVisibility(GONE);
        }
    }


    private void updateRegion() {
        Settings.Region.attachListener(this, this);

        final Region userRegion = User.Region.get();
        final Region settingsRegion = Settings.Region.get();
        final String regionText;

        if (userRegion.equals(settingsRegion)) {
            regionText = userRegion.getName();
        } else {
            regionText = getResources().getString(R.string.x_viewing_y, userRegion.getName(),
                    settingsRegion.getName());
        }

        mRegion.setText(regionText);
    }


}
