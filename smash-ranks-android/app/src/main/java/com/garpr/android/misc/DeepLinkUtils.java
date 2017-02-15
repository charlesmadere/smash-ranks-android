package com.garpr.android.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface DeepLinkUtils {

    @Nullable
    Intent[] buildIntentStack(@NonNull final Activity activity);

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Intent intent);

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final String uri);

    @Nullable
    Intent[] buildIntentStack(@NonNull final Context context, @Nullable final Uri uri);

}
