package com.garpr.android.misc

import android.support.annotation.AnyThread
import android.support.annotation.WorkerThread
import com.garpr.android.models.Region
import com.garpr.android.models.SmashCharacter
import com.garpr.android.models.SmashRoster

interface SmashRosterStorage {

    @WorkerThread
    fun deleteFromStorage(region: Region)

    @AnyThread
    fun getSmashCharacter(region: Region, playerId: String?): SmashCharacter?

    @WorkerThread
    fun writeToStorage(region: Region, smashRoster: SmashRoster?)

}
