package com.garpr.android.activities;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.garpr.android.R;

import butterknife.Bind;


public class FavoritesActivity extends BaseToolbarActivity {


    private static final String TAG = "FavoritesActivity";

    @Bind(R.id.activity_favorites_empty)
    LinearLayout mEmptyView;

    @Bind(R.id.activity_favorites_progress)
    LinearLayout mProgressView;

    @Bind(R.id.activity_favorites_list)
    RecyclerView mRecyclerView;




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
