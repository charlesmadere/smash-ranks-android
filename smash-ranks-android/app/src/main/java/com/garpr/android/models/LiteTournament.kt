package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel

class LiteTournament : AbsTournament, Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            val lt = LiteTournament()
            lt.readFromParcel(it)
            lt
        }
    }

    constructor()

    constructor(id: String, name: String, date: SimpleDate) {
        this.id = id
        this.name = name
        this.date = date
    }

    override val kind = AbsTournament.Kind.LITE

}
