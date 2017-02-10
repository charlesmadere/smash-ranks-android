package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.LiteTournament;
import com.garpr.android.models.Match;
import com.garpr.android.models.Rating;

import java.util.HashMap;
import java.util.Map;

public class PlayerAdapter extends BaseMultiAdapter {

    private static final Map<Class, Integer> LAYOUT_KEY_MAP;


    static {
        LAYOUT_KEY_MAP = new HashMap<>(4);
        LAYOUT_KEY_MAP.put(LiteTournament.class, R.layout.divider_tournament);
        LAYOUT_KEY_MAP.put(Match.class, R.layout.item_match);
        LAYOUT_KEY_MAP.put(Rating.class, R.layout.item_rating);
        LAYOUT_KEY_MAP.put(String.class, R.layout.item_string);
    }

    public PlayerAdapter(@NonNull final Context context) {
        super(context, LAYOUT_KEY_MAP);
    }

    public PlayerAdapter(@NonNull final LayoutInflater layoutInflater) {
        super(layoutInflater, LAYOUT_KEY_MAP);
    }

}
