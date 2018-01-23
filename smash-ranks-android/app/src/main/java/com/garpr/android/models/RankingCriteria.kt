package com.garpr.android.models

interface RankingCriteria {

    val isActive: Boolean

    val rankingActivityDayLimit: Int?

    val rankingNumTourneysAttended: Int?

    val tournamentQualifiedDayLimit: Int?

}
