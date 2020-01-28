package com.garpr.android.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.garpr.android.data.models.Avatar
import com.garpr.android.data.models.Endpoint
import com.garpr.android.data.models.SmashCharacter
import com.garpr.android.data.models.SmashCompetitor

@Entity(
        primaryKeys = [ "endpoint", "id" ],
        tableName = "smashCompetitors"
)
class DbSmashCompetitor(
        @ColumnInfo(name = "avatar") val avatar: Avatar?,
        @ColumnInfo(name = "endpoint") val endpoint: Endpoint,
        @ColumnInfo(name = "mains") val mains: List<SmashCharacter?>?,
        @ColumnInfo(name = "websites") val websites: Map<String, String>?,
        @ColumnInfo(name = "id") val id: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "tag") val tag: String
) {

    constructor(smashCompetitor: SmashCompetitor, endpoint: Endpoint) : this(
            smashCompetitor.avatar, endpoint, smashCompetitor.mains, smashCompetitor.websites,
            smashCompetitor.id, smashCompetitor.name, smashCompetitor.tag)

    fun toSmashCompetitor(): SmashCompetitor {
        return SmashCompetitor(
                avatar = avatar,
                mains = mains,
                websites = websites,
                id = id,
                name = name,
                tag = tag
        )
    }

    override fun toString(): String = tag

}
