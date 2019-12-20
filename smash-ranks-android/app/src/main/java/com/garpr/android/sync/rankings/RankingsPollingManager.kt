package com.garpr.android.sync.rankings

import android.net.Uri
import com.garpr.android.data.models.PollFrequency

interface RankingsPollingManager {

    var isChargingRequired: Boolean

    var isEnabled: Boolean

    var isVibrationEnabled: Boolean

    var isWifiRequired: Boolean

    var pollFrequency: PollFrequency

    var ringtone: Uri?

    fun enableOrDisable()

}
