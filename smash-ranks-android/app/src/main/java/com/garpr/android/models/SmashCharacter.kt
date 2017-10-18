package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import com.garpr.android.R
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

enum class SmashCharacter(
        @param:StringRes val textResId: Int
) : Parcelable {

    @SerializedName("bow")
    BOWSER(R.string.bowser),

    @SerializedName("fcn")
    CAPTAIN_FALCON(R.string.captain_falcon),

    @SerializedName("dnk")
    DONKEY_KONG(R.string.donkey_kong),

    @SerializedName("doc")
    DR_MARIO(R.string.dr_mario),

    @SerializedName("fco")
    FALCO(R.string.falco),

    @SerializedName("fox")
    FOX(R.string.fox),

    @SerializedName("ice")
    ICE_CLIMBERS(R.string.ice_climbers),

    @SerializedName("puf")
    JIGGLYPUFF(R.string.jigglypuff),

    @SerializedName("kby")
    KIRBY(R.string.kirby),

    @SerializedName("lnk")
    LINK(R.string.link),

    @SerializedName("lgi")
    LUIGI(R.string.luigi),

    @SerializedName("mar")
    MARIO(R.string.mario),

    @SerializedName("mrt")
    MARTH(R.string.marth),

    @SerializedName("mew")
    MEWTWO(R.string.mewtwo),

    @SerializedName("gnw")
    MR_GAME_AND_WATCH(R.string.mr_game_and_watch),

    @SerializedName("nes")
    NESS(R.string.ness),

    @SerializedName("pch")
    PEACH(R.string.peach),

    @SerializedName("pch")
    PICHU(R.string.pichu),

    @SerializedName("pik")
    PIKACHU(R.string.pikachu),

    @SerializedName("roy")
    ROY(R.string.roy),

    @SerializedName("sam")
    SAMUS(R.string.samus),

    @SerializedName("shk")
    SHEIK(R.string.sheik),

    @SerializedName("ysh")
    YOSHI(R.string.yoshi),

    @SerializedName("ylk")
    YOUNG_LINK(R.string.young_link),

    @SerializedName("zld")
    ZELDA(R.string.zelda);


    companion object {
        @JvmField
        val CREATOR = createParcel { values()[it.readInt()] }
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(ordinal)
    }

}
