package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel

class LiteTournament(
        regions: List<String>? = null,
        date: SimpleDate,
        id: String,
        name: String
) : AbsTournament(
        regions,
        date,
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { LiteTournament(it.createStringArrayList(),
                it.readParcelable(SimpleDate::class.java.classLoader), it.readString(),
                it.readString()) }
    }

    override val kind
        get() = Kind.LITE

}
