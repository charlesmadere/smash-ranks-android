package com.garpr.android.calls;


import com.garpr.android.misc.Constants;
import com.garpr.android.models.Region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public final class Regions {


    public static void get(final Response<ArrayList<Region>> response, final boolean ignoreCache) {
        new RegionsCall(response, ignoreCache).make();
    }




    private static final class RegionsCall extends Call<ArrayList<Region>> {


        private static final String TAG = "RegionsCall";


        private RegionsCall(final Response<ArrayList<Region>> response, final boolean ignoreCache)
                throws IllegalArgumentException {
            super(response, ignoreCache);
        }


        @Override
        String getCallName() {
            return TAG;
        }


        @Override
        String getUrl() {
            return super.getUrl() + Constants.REGIONS;
        }


        @Override
        void onJSONResponse(final JSONObject json) throws JSONException {
            final JSONArray regionsJSON = json.getJSONArray(Constants.REGIONS);
            final int regionsLength = regionsJSON.length();
            final ArrayList<Region> regions = new ArrayList<>(regionsLength);

            for (int i = 0; i < regionsLength; ++i) {
                final JSONObject regionJSON = regionsJSON.getJSONObject(i);
                final Region region = new Region(regionJSON);
                regions.add(region);
            }

            mResponse.success(regions);
        }


    }


}
