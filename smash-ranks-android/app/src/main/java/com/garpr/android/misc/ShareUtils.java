package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;

public interface ShareUtils {

    void openUrl(@NonNull final Context context, @Nullable final String url);

    void sharePlayer(@NonNull final Activity activity, @NonNull final AbsPlayer player);

    void shareRankings(@NonNull final Activity activity);

    void shareTournament(@NonNull final Activity activity, @NonNull final AbsTournament tournament);

    void shareTournaments(@NonNull final Activity activity);

}
