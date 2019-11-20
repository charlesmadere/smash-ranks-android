package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.Constants
import com.squareup.moshi.Json

enum class Endpoint(
        val basePath: String,
        @StringRes val title: Int
) : Parcelable {

    @Json(name = "gar_pr")
    GAR_PR(Constants.GAR_PR_BASE_PATH, R.string.gar_pr),

    @Json(name = "not_gar_pr")
    NOT_GAR_PR(Constants.NOT_GAR_PR_BASE_PATH, R.string.not_gar_pr);

    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }

        val ALPHABETICAL_ORDER = Comparator<Endpoint> { o1, o2 ->
            o1.name.compareTo(o2.name, ignoreCase = true)
        }
    }

    fun getPlayerWebPath(regionId: String, playerId: String): String {
        return StringBuilder(getWebPath(regionId))
                .append("/players/")
                .append(playerId)
                .toString()
    }

    fun getRankingsWebPath(regionId: String): String {
        return StringBuilder(getWebPath(regionId))
                .append("/rankings")
                .toString()
    }

    fun getTournamentWebPath(regionId: String, tournamentId: String): String {
        return StringBuilder(getTournamentsWebPath(regionId))
                .append('/')
                .append(tournamentId)
                .toString()
    }

    fun getTournamentsWebPath(regionId: String): String {
        return StringBuilder(getWebPath(regionId))
                .append("/tournaments")
                .toString()
    }

    fun getWebPath(regionId: String? = null): String {
        val stringBuilder = StringBuilder(basePath)
                .append("/#/")

        if (regionId?.isNotBlank() == true) {
            stringBuilder.append(regionId)
        }

        return stringBuilder.toString()
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
