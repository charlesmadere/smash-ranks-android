package com.garpr.android.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.garpr.android.R;
import com.garpr.android.calls.Players;
import com.garpr.android.calls.Regions;
import com.garpr.android.calls.Tournaments;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.misc.ResponseOnUi;
import com.garpr.android.misc.Utils;
import com.garpr.android.models.Player;
import com.garpr.android.models.Region;
import com.garpr.android.models.TournamentBundle;
import com.garpr.android.settings.Settings;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


public class DeepLinkActivity extends BaseActivity {


    private static final String TAG = "DeepLinkActivity";

    @Bind(R.id.activity_deep_link_error_container)
    LinearLayout mErrorContainer;

    @Bind(R.id.activity_deep_link_progress_container)
    LinearLayout mProgressContainer;




    private void fetchPlayer(final String playerId, final boolean ignoreCache) {
        Players.get(new ResponseOnUi<ArrayList<Player>>(TAG, DeepLinkActivity.this) {
            @Override
            public void errorOnUi(final Exception e) {
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Player> players) {
                Player player = null;

                for (final Player p : players) {
                    if (p.getId().equalsIgnoreCase(playerId)) {
                        player = p;
                        break;
                    }
                }

                if (player == null) {
                    if (ignoreCache) {
                        showError();
                    } else {
                        fetchPlayer(playerId, true);
                    }
                } else {
                    start(PlayerActivity.IntentBuilder.create(DeepLinkActivity.this, player));
                }
            }
        }, false);
    }


    private void fetchRegion(final String regionId, final boolean ignoreCache,
            final Callback callback) {
        Regions.get(new ResponseOnUi<ArrayList<Region>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Region> regions) {
                Region region = null;

                for (final Region r : regions) {
                    if (r.getId().equalsIgnoreCase(regionId)) {
                        region = r;
                        break;
                    }
                }

                if (region == null) {
                    if (ignoreCache) {
                        showError();
                    } else {
                        fetchRegion(regionId, true, callback);
                    }
                } else {
                    Settings.Region.set(region);
                    callback.finished();
                }
            }
        }, ignoreCache);
    }


    private void fetchRegionThenPlayer(final String regionId, final String playerId) {
        fetchRegion(regionId, false, new Callback() {
            @Override
            public void finished() {
                fetchPlayer(playerId, false);
            }
        });
    }


    private void fetchRegionThenRankings(final String regionId) {
        fetchRegion(regionId, false, new Callback() {
            @Override
            public void finished() {
                start(new RankingsActivity.IntentBuilder(DeepLinkActivity.this));
            }
        });
    }


    private void fetchRegionThenTournament(final String regionId, final String tournamentId) {
        fetchRegion(regionId, false, new Callback() {
            @Override
            public void finished() {
                fetchTournament(tournamentId);
            }
        });
    }


    private void fetchRegionThenTournaments(final String regionId) {
        fetchRegion(regionId, false, new Callback() {
            @Override
            public void finished() {
                start(new TournamentsActivity.IntentBuilder(DeepLinkActivity.this));
            }
        });
    }


    private void fetchTournament(final String tournamentId) {
        Tournaments.getTournament(new ResponseOnUi<TournamentBundle>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                showError();
            }


            @Override
            public void successOnUi(final TournamentBundle tournamentBundle) {
                start(new TournamentActivity.IntentBuilder(DeepLinkActivity.this, tournamentBundle));
            }
        }, tournamentId);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_deep_link;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Settings.OnboardingComplete.get()) {
            Console.d(TAG, "Attempting to deep link...");

            if (getIntent() == null) {
                Console.w(TAG, "Cancelling deep link because Intent is null");
                start(new RankingsActivity.IntentBuilder(this));
            } else {
                parseIntent();
            }
        } else {
            Console.d(TAG, "Deep link cancelled because onboarding is incomplete");
            start(new OnboardingActivity.IntentBuilder(this));
        }
    }


    @OnClick(R.id.activity_deep_link_retry)
    void onRetryClick() {
        Console.d(TAG, "Retrying deep link...");
        showProgress();
        parseIntent();
    }


    private void parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();

        if (uri == null) {
            Console.w(TAG, "Cancelling deep link because Uri is null");
            start(new RankingsActivity.IntentBuilder(this));
        } else if (parseUri(uri)) {
            // intentionally blank
        } else {
            start(new RankingsActivity.IntentBuilder(this));
        }
    }


    private boolean parseUri(final Uri uri) {
        final String uriString = uri.toString();

        if (!Utils.validStrings(uriString)) {
            Console.w(TAG, "Deep link Uri String is invalid");
            return false;
        }

        CrashlyticsManager.setString(Constants.DEEP_LINK_URL, uriString);
        Console.d(TAG, "Deep link Uri: \"" + uriString + '"');

        final String path = uriString.substring(Constants.WEB_URL.length(), uriString.length());

        if (path.contains("/")) {
            final String[] paths = path.split("/");
            parseUriPaths(paths);
        } else {
            fetchRegionThenRankings(path);
        }

        return true;
    }


    private void parseUriPaths(final String[] paths) {
        final String regionId = paths[0];

        if (Constants.PLAYERS.equalsIgnoreCase(paths[1])) {
            if (paths.length == 3 && Utils.validStrings(paths[2])) {
                fetchRegionThenPlayer(regionId, paths[2]);
            } else {
                fetchRegionThenRankings(regionId);
            }
        } else if (Constants.RANKINGS.equalsIgnoreCase(paths[1])) {
            fetchRegionThenRankings(regionId);
        } else if (Constants.TOURNAMENTS.equalsIgnoreCase(paths[1])) {
            if (paths.length == 3 && Utils.validStrings(paths[2])) {
                fetchRegionThenTournament(regionId, paths[2]);
            } else {
                fetchRegionThenTournaments(regionId);
            }
        }
    }


    private void showError() {
        mProgressContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
    }


    private void showProgress() {
        mErrorContainer.setVisibility(View.GONE);
        mProgressContainer.setVisibility(View.VISIBLE);
    }


    private void start(final IntentBuilder intentBuilder) {
        intentBuilder.start(this);
        finish();
    }




    private interface Callback {
        void finished();
    }


}
