package com.garpr.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garpr.android.R;

public class PlayersFragment extends BaseFragment {

    private static final String TAG = "PlayersFragment";


    public static PlayersFragment create() {
        return new PlayersFragment();
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
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

}
