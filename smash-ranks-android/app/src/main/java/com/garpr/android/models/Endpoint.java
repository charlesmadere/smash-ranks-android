package com.garpr.android.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;
import com.google.gson.annotations.SerializedName;

public enum Endpoint implements Parcelable {

    @SerializedName("gar_pr")
    GAR_PR(Constants.GAR_PR_API_PORT, R.string.gar_pr, Constants.GAR_PR_BASE_PATH),

    @SerializedName("not_gar_pr")
    NOT_GAR_PR(Constants.NOT_GAR_PR_API_PORT, R.string.not_gar_pr, Constants.NOT_GAR_PR_BASE_PATH);

    private final int mPort;

    @StringRes
    private final int mName;

    @NonNull
    private final String mPath;


    Endpoint(final int port, @StringRes final int name, @NonNull final String path) {
        mPort = port;
        mName = name;
        mPath = path;
    }

    @NonNull
    public String getApiPath() {
        return getBasePath() + ":" + mPort + "/";
    }

    @NonNull
    public String getBasePath() {
        return mPath;
    }

    @StringRes
    public int getName() {
        return mName;
    }

    @NonNull
    public String getHeadToHeadApiPath(@NonNull final String regionId,
            @NonNull final String playerId, @NonNull final String opponentId) {
        return Uri.parse(getMatchesApiPath(regionId, playerId))
                .buildUpon()
                .appendQueryParameter("opponent", opponentId)
                .build()
                .toString();
    }

    @NonNull
    public String getMatchesApiPath(@NonNull final String regionId, @NonNull final String playerId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("matches")
                .appendPath(playerId)
                .build()
                .toString();
    }

    @NonNull
    public String getPlayerApiPath(@NonNull final String regionId, @NonNull final String playerId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("players")
                .appendPath(playerId)
                .build()
                .toString();
    }

    @NonNull
    public String getPlayersApiPath(@NonNull final String regionId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("players")
                .build()
                .toString();
    }

    @NonNull
    public String getPlayerWebPath(@NonNull final String regionId, @NonNull final String playerId) {
        return getWebPath(regionId) + "/players/" + playerId;
    }

    @NonNull
    public String getRankingsApiPath(@NonNull final String regionId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("rankings")
                .build()
                .toString();
    }

    @NonNull
    public String getRegionsApiPath() {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath("regions")
                .build()
                .toString();
    }

    @NonNull
    public String getTournamentApiPath(@NonNull final String regionId,
            @NonNull final String tournamentId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("tournaments")
                .appendPath(tournamentId)
                .build()
                .toString();
    }

    @NonNull
    public String getTournamentsApiPath(@NonNull final String regionId) {
        return Uri.parse(getApiPath())
                .buildUpon()
                .appendPath(regionId)
                .appendPath("tournaments")
                .build()
                .toString();
    }

    @NonNull
    public String getTournamentWebPath(@NonNull final String regionId,
            @NonNull final String tournamentId) {
        return getWebPath(regionId) + "/tournaments/" + tournamentId;
    }

    @NonNull
    public String getWebPath() {
        return getBasePath() + "/#/";
    }

    @NonNull
    public String getWebPath(@NonNull final String regionId) {
        return getWebPath() + regionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<Endpoint> CREATOR = new Creator<Endpoint>() {
        @Override
        public Endpoint createFromParcel(final Parcel source) {
            return values()[source.readInt()];
        }

        @Override
        public Endpoint[] newArray(final int size) {
            return new Endpoint[size];
        }
    };

}
