package com.garpr.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Ranking;
import com.garpr.android.networking.ApiListener;
import com.garpr.android.networking.ServerApi;

import javax.inject.Inject;

public class PlayerActivity extends BaseActivity {

    private static final String TAG = "PlayerActivity";
    private static final String CNAME = PlayerActivity.class.getCanonicalName();
    private static final String EXTRA_PLAYER_ID = CNAME + ".PlayerId";
    private static final String EXTRA_PLAYER_NAME = CNAME + ".PlayerName";

    @Inject
    ServerApi mServerApi;


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
        App.get().getAppComponent().inject(this);
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

    private final ApiListener<FullPlayer> mFullPlayerListener = new ApiListener<FullPlayer>() {
        @Override
        public void failure() {

        }

        @Override
        public boolean isAlive() {
            return PlayerActivity.this.isAlive();
        }

        @Override
        public void success(@Nullable final FullPlayer object) {

        }
    };

    private final ApiListener<MatchesBundle> mMatchesBundleListener = new ApiListener<MatchesBundle>() {
        @Override
        public void failure() {

        }

        @Override
        public boolean isAlive() {
            return PlayerActivity.this.isAlive();
        }

        @Override
        public void success(@Nullable final MatchesBundle object) {

        }
    };

}
