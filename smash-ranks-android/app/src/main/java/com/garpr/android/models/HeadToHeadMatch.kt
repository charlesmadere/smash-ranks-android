package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.google.gson.annotations.SerializedName

data class HeadToHeadMatch(
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("opponent") val opponent: AbsPlayer,
        @SerializedName("result") val result: MatchResult
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { HeadToHeadMatch(it.readAbsPlayer(), it.readAbsPlayer(),
                it.readParcelable(MatchResult::class.java.classLoader)) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeParcelable(result, flags)
    }

}
