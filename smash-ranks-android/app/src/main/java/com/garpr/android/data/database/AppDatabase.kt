package com.garpr.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ DbFavoritePlayer::class ], version = 1)
@TypeConverters(RegionConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dbFavoritePlayerDao(): FavoritePlayerDao

}
