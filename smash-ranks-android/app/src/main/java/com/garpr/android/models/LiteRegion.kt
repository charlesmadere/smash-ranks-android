package com.garpr.android.models

import android.os.Parcelable
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.readInteger

class LiteRegion(
        rankingActivityDayLimit: Int? = null,
        rankingNumTourneysAttended: Int? = null,
        tournamentQualifiedDayLimit: Int? = null,
        displayName: String,
        id: String
) : AbsRegion(
        rankingActivityDayLimit,
        rankingNumTourneysAttended,
        tournamentQualifiedDayLimit,
        displayName,
        id
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel { LiteRegion(it.readInteger(), it.readInteger(),
                it.readInteger(), it.readString(), it.readString()) }
    }

    override val kind
        get() = Kind.LITE

}
