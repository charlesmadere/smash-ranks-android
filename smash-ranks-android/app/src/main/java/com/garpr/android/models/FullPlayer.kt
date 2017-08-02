package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.google.gson.annotations.SerializedName
import java.util.*

class FullPlayer : AbsPlayer(), Parcelable {

    @SerializedName("aliases")
    var aliases: ArrayList<String>? = null
        private set

    @SerializedName("regions")
    var regions: ArrayList<String>? = null
        private set

    @SerializedName("ratings")
    var ratings: Ratings? = null
        private set


    companion object {
        @JvmField
        val CREATOR = createParcel {
            val fp = FullPlayer()
            fp.readFromParcel(it)
            fp
        }
    }

    override val kind = AbsPlayer.Kind.FULL

    fun hasAliases(): Boolean {
        return aliases != null && !aliases!!.isEmpty()
    }

    fun hasRatings(): Boolean {
        return ratings != null && !ratings!!.isEmpty()
    }

    fun hasRegions(): Boolean {
        return regions != null && !regions!!.isEmpty()
    }

    override fun readFromParcel(source: Parcel) {
        super.readFromParcel(source)
        aliases = source.createStringArrayList()
        regions = source.createStringArrayList()
        ratings = source.readParcelable<Ratings>(Ratings::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeStringList(aliases)
        dest.writeStringList(regions)
        dest.writeParcelable(ratings, flags)
    }

}
