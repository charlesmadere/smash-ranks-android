package com.garpr.android.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritePlayerDao {

    @Delete
    fun delete(dbFavoritePlayer: DbFavoritePlayer)

    @Query("DELETE FROM favoritePlayers")
    fun deleteAll()

    @Query("SELECT * FROM favoritePlayers")
    fun getAll(): List<DbFavoritePlayer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbFavoritePlayer: DbFavoritePlayer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dbFavoritePlayers: List<DbFavoritePlayer>)

}
