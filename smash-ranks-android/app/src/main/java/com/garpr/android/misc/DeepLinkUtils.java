package com.garpr.android.misc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.Endpoint;
import com.garpr.android.models.Region;
import com.garpr.android.models.RegionsBundle;

public interface DeepLinkUtils {

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Intent intent,
            @NonNull final Region region);

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final String uri,
            @NonNull final Region region);

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Uri uri,
            @NonNull final Region region);

    @Nullable
    Endpoint getEndpoint(@Nullable final Intent intent);

    @Nullable
    Endpoint getEndpoint(@Nullable final String uri);

    @Nullable
    Endpoint getEndpoint(@Nullable final Uri uri);

    @Nullable
    Region getRegion(@Nullable final RegionsBundle regionsBundle, @Nullable final Endpoint endpoint);

    boolean isValidUri(@Nullable final Intent intent);

    boolean isValidUri(@Nullable final String uri);

    boolean isValidUri(@Nullable final Uri uri);

}
