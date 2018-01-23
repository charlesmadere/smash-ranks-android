package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readOptionalBoolean
import com.garpr.android.extensions.readOptionalInteger

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
        val CREATOR = createParcel { LiteRegion(it.readOptionalBoolean(), it.readOptionalInteger(),
                it.readOptionalInteger(), it.readOptionalInteger(), it.readString(),
                it.readString()) }
    }

    override val kind
        get() = Kind.LITE

}
