package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel

class LitePlayer : AbsPlayer, Parcelable {

    companion object {
        val CREATOR = createParcel {
            val lp = LitePlayer()
            lp.readFromParcel(it)
            lp
        }
    }

    constructor()

    constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }

    override val kind = AbsPlayer.Kind.LITE

}
