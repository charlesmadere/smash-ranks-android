package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

enum class MeleeCharacter : Parcelable {

    @SerializedName("bow")
    BOWSER,

    @SerializedName("fcn")
    CAPTAIN_FALCON,

    @SerializedName("dnk")
    DONKEY_KONG,

    @SerializedName("doc")
    DR_MARIO,

    @SerializedName("fco")
    FALCO,

    @SerializedName("fox")
    FOX,

    @SerializedName("ice")
    ICE_CLIMBERS,

    @SerializedName("puf")
    JIGGLYPUFF,

    @SerializedName("kby")
    KIRBY,

    @SerializedName("lnk")
    LINK,

    @SerializedName("lgi")
    LUIGI,

    @SerializedName("mar")
    MARIO,

    @SerializedName("mrt")
    MARTH,

    @SerializedName("mew")
    MEWTWO,

    @SerializedName("gnw")
    MR_GAME_AND_WATCH,

    @SerializedName("nes")
    NESS,

    @SerializedName("pch")
    PEACH,

    @SerializedName("pch")
    PICHU,

    @SerializedName("pik")
    PIKACHU,

    @SerializedName("roy")
    ROY,

    @SerializedName("sam")
    SAMUS,

    @SerializedName("shk")
    SHEIK,

    @SerializedName("ysh")
    YOSHI,

    @SerializedName("ylk")
    YOUNG_LINK,

    @SerializedName("zld")
    ZELDA;


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
