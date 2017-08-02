package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName

class FavoritePlayer : AbsPlayer, Parcelable {

    @SerializedName("region")
    lateinit var region: Region
        private set


    companion object {
        @JvmField
        val CREATOR = createParcel {
            val fp = FavoritePlayer()
            fp.readFromParcel(it)
            fp
        }
    }

    constructor()

    constructor(player: AbsPlayer, region: Region) {
        this.id = player.id
        this.name = player.name
        this.region = region
    }

    override val kind = AbsPlayer.Kind.FAVORITE

    override fun readFromParcel(source: Parcel) {
        super.readFromParcel(source)
        region = source.readParcelable(Region::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(region, flags)
    }

}
