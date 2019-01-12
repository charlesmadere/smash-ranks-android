package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireString

class LitePlayer(
        id: String,
        name: String
) : AbsPlayer(
        id,
        name
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { LitePlayer(it.requireString(), it.requireString()) }
    }

    override val kind: Kind
        get() = Kind.LITE

}
