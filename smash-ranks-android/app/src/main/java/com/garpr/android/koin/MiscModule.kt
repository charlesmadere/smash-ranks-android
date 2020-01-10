package com.garpr.android.koin

import com.garpr.android.data.converters.AbsPlayerConverter
import com.garpr.android.data.converters.AbsRegionConverter
import com.garpr.android.data.converters.AbsTournamentConverter
import com.garpr.android.data.converters.FullTournamentMatchConverter
import com.garpr.android.data.converters.RankedPlayerConverter
import com.garpr.android.data.converters.SimpleDateConverter
import com.garpr.android.data.converters.TournamentMatchConverter
import com.garpr.android.features.player.SmashRosterAvatarUrlHelper
import com.garpr.android.features.player.SmashRosterAvatarUrlHelperImpl
import com.garpr.android.features.rankings.PreviousRankUtils
import com.garpr.android.features.rankings.PreviousRankUtilsImpl
import com.garpr.android.misc.Constants
import com.garpr.android.misc.PlayerListBuilder
import com.garpr.android.misc.PlayerListBuilderImpl
import com.garpr.android.misc.RegionHandleUtils
import com.garpr.android.misc.RegionHandleUtilsImpl
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.SchedulersImpl
import com.garpr.android.misc.ShareUtils
import com.garpr.android.misc.ShareUtilsImpl
import com.garpr.android.misc.Timber
import com.garpr.android.misc.TimberImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module

val miscModule = module {

    single<Moshi> {
        Moshi.Builder()
                .add(AbsPlayerConverter)
                .add(AbsRegionConverter)
                .add(AbsTournamentConverter)
                .add(FullTournamentMatchConverter)
                .add(RankedPlayerConverter)
                .add(SimpleDateConverter)
                .add(TournamentMatchConverter)
                .add(KotlinJsonAdapterFactory()) // this has to be at the end
                .build()
    }

    single<PlayerListBuilder> { PlayerListBuilderImpl(get()) }
    single<PreviousRankUtils> { PreviousRankUtilsImpl() }
    single<RegionHandleUtils> { RegionHandleUtilsImpl(get()) }
    single<Schedulers> { SchedulersImpl(get()) }
    single<ShareUtils> { ShareUtilsImpl(get(), get()) }
    single<SmashRosterAvatarUrlHelper> { SmashRosterAvatarUrlHelperImpl(Constants.SMASH_ROSTER_BASE_PATH) }
    single<Timber> { TimberImpl(get(), get()) }

}
