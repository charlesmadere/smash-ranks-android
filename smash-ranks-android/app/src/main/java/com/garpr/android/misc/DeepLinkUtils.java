package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.garpr.android.models.Endpoint;

public interface DeepLinkUtils {

    @Nullable
    Details buildIntentStack(@NonNull final Activity activity);

    @Nullable
    Details buildIntentStack(@NonNull final Context context, @Nullable final Intent intent);

    @Nullable
    Details buildIntentStack(@NonNull final Context context, @Nullable final String uri);

    @Nullable
    Details buildIntentStack(@NonNull final Context context, @Nullable final Uri uri);

    @Nullable
    Endpoint getEndpoint(@NonNull final Activity activity);

    @Nullable
    Endpoint getEndpoint(@NonNull final Context context, @Nullable final Intent intent);

    @Nullable
    Endpoint getEndpoint(@NonNull final Context context, @Nullable final String uri);

    @Nullable
    Endpoint getEndpoint(@NonNull final Context context, @Nullable final Uri uri);

    boolean isValidUri(@Nullable final String uri);


    class Details {
        @NonNull
        public final Endpoint mEndpoint;

        @NonNull
        public final Intent[] mIntents;

        @NonNull
        public final String mRegionId;

        protected Details(@NonNull final Endpoint endpoint, @NonNull final Intent[] intents,
                @NonNull final String regionId) {
            mEndpoint = endpoint;
            mIntents = intents;
            mRegionId = regionId;
        }
    }

}
