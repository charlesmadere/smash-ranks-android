package com.garpr.android.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.garpr.android.R;
import com.garpr.android.models.Match;
import com.garpr.android.models.MatchesBundle;
import com.garpr.android.models.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerAdapter extends BaseMultiAdapter {

    private static final Map<Class, Integer> LAYOUT_KEY_MAP;


    static {
        LAYOUT_KEY_MAP = new HashMap<>(3);
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

    public void set(@Nullable final Rating rating, @Nullable final MatchesBundle matchesBundle) {
        final ArrayList<Object> list = new ArrayList<>();

        if (rating != null) {
            list.add(rating);
        }

        if (matchesBundle != null && matchesBundle.hasMatches()) {
            list.addAll(matchesBundle.getMatches());
        } else {
            list.add(getContext().getString(R.string.no_matches));
        }

        set(list);
    }

}
