package com.garpr.android.activities;


import android.content.Context;

import com.garpr.android.settings.Settings;


public class ProfileActivity extends PlayerActivity {


    public static class IntentBuilder extends PlayerActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, Settings.User.Player.get());
        }


    }


}
