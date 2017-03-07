package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.views.FavoritePlayersLayout;

import butterknife.BindView;

public class FavoritePlayersFragment extends BaseSearchableFragment {

    private static final String TAG = "FavoritePlayersFragment";

    @BindView(R.id.favoritePlayersLayout)
    FavoritePlayersLayout mFavoritePlayersLayout;


    public static FavoritePlayersFragment create() {
        return new FavoritePlayersFragment();
    }

    @Override
    protected String getFragmentName() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.favorite_players_layout, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFavoritePlayersLayout.refresh();
    }

    @Override
    public void search(@Nullable final String query) {
        mFavoritePlayersLayout.search(query);
    }

}
