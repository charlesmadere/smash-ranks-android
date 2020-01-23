package com.garpr.android.koin

import com.garpr.android.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {

    single {
        val appDatabase: AppDatabase = get()
        appDatabase.dbFavoritePlayerDao()
    }

}
