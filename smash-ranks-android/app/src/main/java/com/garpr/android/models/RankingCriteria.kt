package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.createParcel

interface RankingCriteria : Parcelable {

    val activeTf: Boolean

    val rankingCriteriaKind: Kind

    val rankingActivityDayLimit: Int?

    val rankingNumTourneysAttended: Int?

    val tournamentQualifiedDayLimit: Int?


    enum class Kind : Parcelable {
        IMPL;

        companion object {
            @JvmField
            val CREATOR = createParcel { Kind.values()[it.readInt()] }
        }

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(ordinal)
        }
    }

}
