package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.optAbsTournamentList
import com.garpr.android.extensions.writeAbsTournamentList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentsBundle(
        @Json(name = "tournaments") val tournaments: List<AbsTournament>? = null
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsTournamentList(tournaments, flags)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            TournamentsBundle(
                    it.optAbsTournamentList()
            )
        }
    }

}
