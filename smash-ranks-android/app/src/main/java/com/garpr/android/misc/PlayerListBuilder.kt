package com.garpr.android.misc

import androidx.annotation.WorkerThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.PlayersBundle

interface PlayerListBuilder {

    @WorkerThread
    fun create(bundle: PlayersBundle?, identity: AbsPlayer?): List<PlayerListItem>?

    @WorkerThread
    fun refresh(list: List<PlayerListItem>?, identity: AbsPlayer?): List<PlayerListItem>?

    @WorkerThread
    fun search(list: List<PlayerListItem>?, query: String?): List<PlayerListItem>?

    sealed class PlayerListItem {
        abstract val listId: Long

        sealed class Divider : PlayerListItem() {
            object Digit : Divider() {
                override val listId: Long = Long.MAX_VALUE - 1L
            }

            class Letter(
                    override val listId: Long,
                    val letter: String
            ) : Divider()

            object Other : Divider() {
                override val listId: Long = Long.MAX_VALUE - 2L
            }
        }

        class NoResults(
                val query: String
        ) : PlayerListItem() {
            override val listId: Long = Long.MAX_VALUE - 3L
        }

        class Player(
                val player: AbsPlayer,
                val isIdentity: Boolean
        ) : PlayerListItem() {
            override val listId: Long = player.hashCode().toLong()
        }
    }

}
