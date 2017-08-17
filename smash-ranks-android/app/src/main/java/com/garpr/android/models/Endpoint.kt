package com.garpr.android.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.Constants
import com.google.gson.annotations.SerializedName

enum class Endpoint(
        port: Int,
        @param:StringRes val title: Int,
        val basePath: String
) : Parcelable {

    @SerializedName("gar_pr")
    GAR_PR(Constants.GAR_PR_API_PORT, R.string.gar_pr, Constants.GAR_PR_BASE_PATH),

    @SerializedName("not_gar_pr")
    NOT_GAR_PR(Constants.NOT_GAR_PR_API_PORT, R.string.not_gar_pr, Constants.NOT_GAR_PR_BASE_PATH);


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    val apiPath = "$basePath:$port/"

    fun getHeadToHeadApiPath(regionId: String, playerId: String, opponentId: String): String {
        return Uri.parse(getMatchesApiPath(regionId, playerId))
                .buildUpon()
                .appendQueryParameter("opponent", opponentId)
                .build()
                .toString()
    }

    fun getMatchesApiPath(regionId: String, playerId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("matches")
                .appendPath(playerId)
                .build()
                .toString()
    }

    fun getPlayerApiPath(regionId: String, playerId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("players")
                .appendPath(playerId)
                .build()
                .toString()
    }

    fun getPlayersApiPath(regionId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("players")
                .build()
                .toString()
    }

    fun getPlayerWebPath(regionId: String, playerId: String): String {
        return getWebPath(regionId) + "/players/" + playerId
    }

    fun getRankingsApiPath(regionId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("rankings")
                .build()
                .toString()
    }

    fun getRankingsWebPath(regionId: String): String {
        return getWebPath(regionId) + "/rankings"
    }

    fun getTournamentApiPath(regionId: String, tournamentId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("tournaments")
                .appendPath(tournamentId)
                .build()
                .toString()
    }

    fun getTournamentsApiPath(regionId: String): String {
        return Uri.parse(apiPath)
                .buildUpon()
                .appendPath(regionId)
                .appendPath("tournaments")
                .build()
                .toString()
    }

    fun getTournamentWebPath(regionId: String, tournamentId: String): String {
        return getTournamentsWebPath(regionId) + "/" + tournamentId
    }

    fun getTournamentsWebPath(regionId: String): String {
        return getWebPath(regionId) + "/tournaments"
    }

    fun getWebPath(regionId: String? = null): String {
        val stringBuilder = StringBuilder(basePath)
                .append("/#/")

        if (regionId?.isNotBlank() == true) {
            stringBuilder.append(regionId)
        }

        return stringBuilder.toString()
    }

    val regionsApiPath = Uri.parse(apiPath)
            .buildUpon()
            .appendPath("regions")
            .build()
            .toString()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
