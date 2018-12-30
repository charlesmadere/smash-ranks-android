package com.garpr.android.models

import android.os.Parcelable

interface RankingCriteria : Parcelable {

    val isActive: Boolean

    val rankingActivityDayLimit: Int?

    val rankingNumTourneysAttended: Int?

    val tournamentQualifiedDayLimit: Int?

}
