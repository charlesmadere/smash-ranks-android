package com.garpr.android.misc;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;

public interface ShareUtils {

    void sharePlayer(@NonNull final Activity activity, @NonNull final AbsPlayer player);

    void shareTournament(@NonNull final Activity activity, @NonNull final AbsTournament tournament);

}
