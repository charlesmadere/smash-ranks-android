package com.garpr.android.data.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.requireString

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
        val CREATOR = createParcel {
            LiteTournament(
                    it.createStringArrayList(),
                    it.requireParcelable(SimpleDate::class.java.classLoader),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override val kind: Kind
        get() = Kind.LITE

}
