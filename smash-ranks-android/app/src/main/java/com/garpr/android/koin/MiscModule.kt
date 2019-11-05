package com.garpr.android.koin

import com.garpr.android.data.converters.AbsPlayerConverter
import com.garpr.android.data.converters.AbsRegionConverter
import com.garpr.android.data.converters.AbsTournamentConverter
import com.garpr.android.data.converters.MatchConverter
import com.garpr.android.data.converters.RankedPlayerConverter
import com.garpr.android.data.converters.SimpleDateConverter
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperImpl
import com.garpr.android.features.rankings.PreviousRankUtils
import com.garpr.android.features.rankings.PreviousRankUtilsImpl
import com.garpr.android.misc.Constants
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.SchedulersImpl
import com.garpr.android.misc.ShareUtils
import com.garpr.android.misc.ShareUtilsImpl
import com.garpr.android.misc.Timber
import com.garpr.android.misc.TimberImpl
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val miscModule = module {

    single<Moshi> {
        Moshi.Builder()
                .add(AbsPlayerConverter)
                .add(AbsRegionConverter)
                .add(AbsTournamentConverter)
                .add(MatchConverter)
                .add(RankedPlayerConverter)
                .add(SimpleDateConverter)
                .build()
    }

    single<PreviousRankUtils> { PreviousRankUtilsImpl() }
    single<Schedulers> { SchedulersImpl(get()) }
    single<ShareUtils> { ShareUtilsImpl(get(), get()) }
    single<SmashRosterAvatarUrlHelper> { SmashRosterAvatarUrlHelperImpl(Constants.SMASH_ROSTER_BASE_PATH) }
    single<Timber> { TimberImpl(get(), get()) }

}