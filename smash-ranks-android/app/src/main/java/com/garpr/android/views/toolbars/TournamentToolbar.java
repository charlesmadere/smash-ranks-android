package com.garpr.android.views.toolbars;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;

import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.FullTournament;

public class TournamentToolbar extends SearchToolbar {

    public TournamentToolbar(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentToolbar(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onCreateOptionsMenu(final MenuInflater inflater, final Menu menu) {
        inflater.inflate(R.menu.toolbar_tournament, menu);
        super.onCreateOptionsMenu(inflater, menu);
    }

    @Override
    public void onRefreshMenu() {
        super.onRefreshMenu();

        if (isSearchLayoutExpanded()) {
            return;
        }

        final Activity activity = MiscUtils.optActivity(getContext());
        final FullTournament fullTournament;

        if (activity instanceof DataProvider) {
            fullTournament = ((DataProvider) activity).getFullTournament();
        } else {
            fullTournament = null;
        }

        if (fullTournament == null) {
            return;
        }

        final Menu menu = getMenu();
        menu.findItem(R.id.miShare).setVisible(true);

        if (!TextUtils.isEmpty(fullTournament.getUrl())) {
            menu.findItem(R.id.miViewTournamentPage).setVisible(true);
        }
    }


    public interface DataProvider {
        @Nullable
        FullTournament getFullTournament();
    }

}
