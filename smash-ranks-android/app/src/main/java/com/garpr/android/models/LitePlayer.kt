package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel

class LitePlayer(
        id: String,
        name: String
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { LitePlayer(it.readString(), it.readString()) }
    }

    override val kind
        get() = Kind.LITE

}
