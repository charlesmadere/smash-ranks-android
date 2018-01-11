package com.garpr.android.views

import com.garpr.android.models.AbsPlayer

interface IdentityView {

    fun clear() {
        identity = null
        identityId = null
    }

    var identity: AbsPlayer?

    var identityId: String?

}
