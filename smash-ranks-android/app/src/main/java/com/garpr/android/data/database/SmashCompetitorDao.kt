package com.garpr.android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.garpr.android.data.models.Endpoint

@Dao
interface SmashCompetitorDao {

    @Query("DELETE FROM smashCompetitors")
    fun deleteAll()

    @Query("SELECT * FROM smashCompetitors WHERE endpoint = :endpoint AND id = :id LIMIT 1")
    fun get(endpoint: Endpoint, id: String): DbSmashCompetitor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dbSmashCompetitors: List<DbSmashCompetitor>)

}
