package com.garpr.android.koin

import androidx.room.Room
import com.garpr.android.data.database.AppDatabase
import com.garpr.android.misc.ThreadUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        val threadUtils: ThreadUtils = get()
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME)
                .setQueryExecutor(threadUtils.background)
                .setTransactionExecutor(threadUtils.background)
                .build()
    }

    single {
        val appDatabase: AppDatabase = get()
        appDatabase.dbFavoritePlayerDao()
    }

}
