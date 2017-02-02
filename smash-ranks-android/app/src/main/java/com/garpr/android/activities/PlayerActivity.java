package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.Ranking;

public class PlayerActivity extends BaseActivity {

    private static final String TAG = "PlayerActivity";
    private static final String CNAME = PlayerActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";


    public static Intent getLaunchIntent(final Context context, @NonNull final AbsPlayer player) {
        return getLaunchIntent(context, player.getId(), player.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final Ranking ranking) {
        return getLaunchIntent(context, ranking.getId(), ranking.getName());
    }

    public static Intent getLaunchIntent(final Context context, @NonNull final String playerId,
            @Nullable final String playerName) {
        final Intent intent = new Intent(context, PlayerActivity.class)
                .putExtra(EXTRA_PLAYER_ID, playerId);

        if (!TextUtils.isEmpty(playerName)) {
            intent.putExtra(EXTRA_PLAYER_NAME, playerName);
        }

        return intent;
    }

    @Override
    protected String getActivityName() {
        return TAG;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PLAYER_NAME)) {
            setTitle(intent.getStringExtra(EXTRA_PLAYER_NAME));
        }

        // TODO
    }

    @Override
    protected boolean showUpNavigation() {
        return true;
    }

}
