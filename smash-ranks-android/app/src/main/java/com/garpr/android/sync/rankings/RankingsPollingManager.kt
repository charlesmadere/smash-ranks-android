package com.garpr.android.sync.rankings

import com.garpr.android.models.PollFrequency

interface RankingsPollingManager {

    fun enableOrDisable()

    var isChargingRequired: Boolean

    var isEnabled: Boolean

    var isWifiRequired: Boolean

    var pollFrequency: PollFrequency

}