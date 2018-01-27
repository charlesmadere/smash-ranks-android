package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.Constants
import com.google.gson.annotations.SerializedName

enum class Endpoint(
        val basePath: String,
        @StringRes val title: Int
) : Parcelable {

    @SerializedName("gar_pr")
    GAR_PR(Constants.GAR_PR_BASE_PATH, R.string.gar_pr),

    @SerializedName("not_gar_pr")
    NOT_GAR_PR(Constants.NOT_GAR_PR_BASE_PATH, R.string.not_gar_pr);


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }

        val ALPHABETICAL_ORDER = Comparator<Endpoint> { o1, o2 ->
            o1.name.compareTo(o2.name, ignoreCase = true)
        }
    }

    fun getPlayerWebPath(regionId: String, playerId: String) =
            getWebPath(regionId) + "/players/" + playerId

    fun getRankingsWebPath(regionId: String) = getWebPath(regionId) + "/rankings"

    fun getTournamentWebPath(regionId: String, tournamentId: String) =
            getTournamentsWebPath(regionId) + "/" + tournamentId

    fun getTournamentsWebPath(regionId: String) = getWebPath(regionId) + "/tournaments"

    fun getWebPath(regionId: String? = null): String {
        val stringBuilder = StringBuilder(basePath)
                .append("/#/")

        if (regionId?.isNotBlank() == true) {
            stringBuilder.append(regionId)
        }

        return stringBuilder.toString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
