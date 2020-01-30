package com.garpr.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
        entities = [ DbFavoritePlayer::class, DbSmashCompetitor::class ],
        version = 1
)
@TypeConverters(
        AvatarConverter::class, EndpointConverter::class, ListOfSmashCharacterConverter::class,
        MapOfStringToStringConverter::class, RegionConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dbFavoritePlayerDao(): FavoritePlayerDao

    abstract fun dbSmashCompetitorDao(): SmashCompetitorDao

}
