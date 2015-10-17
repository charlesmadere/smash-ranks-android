package com.garpr.android.activities;


import android.content.Context;

import com.garpr.android.R;


public class FavoritesActivity extends BaseToolbarActivity {


    private static final String TAG = "FavoritesActivity";




    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_favorites;
    }


    @Override
    protected int getSelectedNavigationItemId() {
        return R.id.navigation_view_menu_favorites;
    }




    public static class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, FavoritesActivity.class);
        }


    }


}
