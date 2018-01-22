package com.garpr.android.models

interface RankingCriteria {

    val activeTf: Boolean

    val rankingActivityDayLimit: Int?

    val rankingNumTourneysAttended: Int?

    val tournamentQualifiedDayLimit: Int?

}
