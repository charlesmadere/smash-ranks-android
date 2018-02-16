package com.garpr.android.misc

import android.content.Context
import android.support.v7.app.AlertDialog
import com.garpr.android.R
import com.garpr.android.misc.FavoritePlayersManager.OnFavoritePlayersChangeListener
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region
import com.garpr.android.preferences.KeyValueStore
import com.google.gson.Gson
import java.lang.ref.WeakReference
import java.util.*

class FavoritePlayersManagerImpl(
        private val gson: Gson,
        private val keyValueStore: KeyValueStore,
        private val timber: Timber
) : FavoritePlayersManager {

    private val listeners = mutableListOf<WeakReference<OnFavoritePlayersChangeListener>>()


    companion object {
        private const val TAG = "FavoritePlayersManagerImpl"
    }

    override val absPlayers: List<AbsPlayer>?
        get() {
            val players = this.players

            if (players == null || players.isEmpty()) {
                return null
            }

            val list = mutableListOf<AbsPlayer>()
            list.addAll(players)

            return list
        }

    override fun addListener(listener: OnFavoritePlayersChangeListener) {
        synchronized (listeners) {
            var addListener = true
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else if (item == listener) {
                    addListener = false
                }
            }

            if (addListener) {
                listeners.add(WeakReference(listener))
            }
        }
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        if (player in this) {
            timber.d(TAG, "Not adding favorite, it already exists in the store")
            return
        }

        timber.d(TAG, "Adding favorite (there are currently $size favorite(s))")

        val favoritePlayer = FavoritePlayer(player.id, player.name, region)
        val playerJson = gson.toJson(favoritePlayer, FavoritePlayer::class.java)
        keyValueStore.setString(player.id, playerJson)
        notifyListeners()
    }

    override fun clear() {
        keyValueStore.clear()
        notifyListeners()
    }

    override fun contains(player: AbsPlayer): Boolean {
        return player.id in this
    }

    override fun contains(playerId: String): Boolean {
        return playerId in keyValueStore
    }

    override val isEmpty: Boolean
        get() {
            val all = keyValueStore.all
            return all == null || all.isEmpty()
        }

    private fun notifyListeners() {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onFavoritePlayersChanged(this)
                }
            }
        }
    }

    override val players: List<FavoritePlayer>?
        get() {
            val all = keyValueStore.all

            if (all == null || all.isEmpty()) {
                return null
            }

            val players = mutableListOf<FavoritePlayer>()

            for ((key, value) in all) {
                val json = value as String
                players.add(gson.fromJson(json, FavoritePlayer::class.java))
            }

            Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
            return players
        }

    override fun removeListener(listener: OnFavoritePlayersChangeListener?) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null || item == listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun removePlayer(player: AbsPlayer) {
        removePlayer(player.id)
    }

    override fun removePlayer(playerId: String) {
        keyValueStore.remove(playerId)
        notifyListeners()
    }

    override fun showAddOrRemovePlayerDialog(context: Context, player: AbsPlayer?, region: Region): Boolean {
        if (player == null) {
            return false
        }

        val builder = AlertDialog.Builder(context)
                .setNegativeButton(R.string.cancel, null)

        if (player in this) {
            builder.setMessage(context.getString(R.string.remove_x_from_favorites, player.name))
                    .setPositiveButton(R.string.yes) { dialog, which -> removePlayer(player) }
        } else {
            builder.setMessage(context.getString(R.string.add_x_to_favorites, player.name))
                    .setPositiveButton(R.string.yes) { dialog, which -> addPlayer(player, region) }
        }

        builder.show()
        return true
    }

    override val size: Int
        get() = keyValueStore.all?.size ?: 0

}
