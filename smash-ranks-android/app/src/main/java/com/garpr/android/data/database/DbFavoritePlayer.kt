package com.garpr.android.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region

@Entity(tableName = "favoritePlayers")
class DbFavoritePlayer(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "region") val region: Region
) {

    constructor(player: AbsPlayer, region: Region) : this(player.id, player.name, region)

    fun toFavoritePlayer(): FavoritePlayer {
        return FavoritePlayer(
                id = id,
                name = name,
                region = region
        )
    }

}
