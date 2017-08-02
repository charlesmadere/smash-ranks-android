package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.misc.ParcelableUtils
import com.google.gson.annotations.SerializedName

data class TournamentsBundle(
        @SerializedName("tournaments") val tournaments: List<AbsTournament>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { TournamentsBundle(ParcelableUtils.readAbsTournamentList(it)) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        ParcelableUtils.writeAbsTournamentList(tournaments, dest, flags)
    }

}
