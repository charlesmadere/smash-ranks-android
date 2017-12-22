package com.garpr.android.sync

interface RankingsPollingSyncManager {

    fun enableOrDisable()

    var isChargingRequired: Boolean

    var isEnabled: Boolean

    var isWifiRequired: Boolean

}
