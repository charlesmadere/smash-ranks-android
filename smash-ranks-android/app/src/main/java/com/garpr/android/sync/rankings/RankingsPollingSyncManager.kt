package com.garpr.android.sync.rankings

interface RankingsPollingSyncManager {

    fun enableOrDisable()

    var isChargingRequired: Boolean

    var isEnabled: Boolean

    var isWifiRequired: Boolean

}
