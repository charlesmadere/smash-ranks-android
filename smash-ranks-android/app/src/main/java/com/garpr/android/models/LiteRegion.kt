package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readBoolean
import com.garpr.android.extensions.readInteger
import com.garpr.android.extensions.requireString

class LiteRegion(
        activeTf: Boolean? = null,
        rankingActivityDayLimit: Int? = null,
        rankingNumTourneysAttended: Int? = null,
        tournamentQualifiedDayLimit: Int? = null,
        displayName: String,
        id: String
) : AbsRegion(
        activeTf,
        rankingActivityDayLimit,
        rankingNumTourneysAttended,
        tournamentQualifiedDayLimit,
        displayName,
        id
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            LiteRegion(
                    it.readBoolean(),
                    it.readInteger(),
                    it.readInteger(),
                    it.readInteger(),
                    it.requireString(),
                    it.requireString()
            )
        }
    }

    override val kind
        get() = Kind.LITE

}
