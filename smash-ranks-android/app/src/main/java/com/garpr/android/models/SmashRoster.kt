package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readSmashCompetitorMap
import com.garpr.android.extensions.writeSmashCompetitorMap
import com.google.gson.annotations.SerializedName

data class SmashRoster(
        @SerializedName("competitors") val competitors: Map<String, SmashCompetitor>? = null
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { SmashRoster(it.readSmashCompetitorMap()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSmashCompetitorMap(competitors)
    }

}
