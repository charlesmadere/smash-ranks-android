package com.garpr.android.sync.rankings

import com.garpr.android.data.models.PollFrequency
import java.net.URI as JavaUri

interface RankingsPollingManager {

    var isChargingRequired: Boolean

    var isEnabled: Boolean

    var isVibrationEnabled: Boolean

    var isWifiRequired: Boolean

    var ringtone: JavaUri?

    var pollFrequency: PollFrequency

    fun enableOrDisable()

}
