package com.garpr.android.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `smashCompetitors` (`avatar` TEXT, `endpoint` INTEGER NOT NULL, `mains` TEXT, `websites` TEXT, `id` TEXT NOT NULL, `name` TEXT NOT NULL, `tag` TEXT NOT NULL, PRIMARY KEY (`endpoint`, `id`))")
        }
    }

}
