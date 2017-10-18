package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readAbsPlayer
import com.garpr.android.extensions.writeAbsPlayer
import com.google.gson.annotations.SerializedName

class HeadToHeadMatch(
        result: MatchResult,
        @SerializedName("player") val player: AbsPlayer,
        @SerializedName("opponent") val opponent: AbsPlayer
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { HeadToHeadMatch(it.readParcelable(MatchResult::class.java.classLoader),
                it.readAbsPlayer(), it.readAbsPlayer()) }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(player, flags)
        dest.writeAbsPlayer(opponent, flags)
    }

}
