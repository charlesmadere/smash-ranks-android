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
        private val mGson: Gson,
        private val mKeyValueStore: KeyValueStore,
        private val mTimber: Timber
) : FavoritePlayersManager {

    private val mListeners: MutableList<WeakReference<OnFavoritePlayersChangeListener>> = mutableListOf()


    companion object {
        private const val TAG = "FavoritePlayersManagerImpl"
    }

    override val absPlayers: List<AbsPlayer>?
        get() {
            val players = players

            if (players == null || players.isEmpty()) {
                return null
            }

            return ArrayList<AbsPlayer>(players)
        }

    override fun addListener(listener: OnFavoritePlayersChangeListener) {
        synchronized (mListeners) {
            var addListener = true
            val iterator = mListeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else if (item === listener) {
                    addListener = false
                }
            }

            if (addListener) {
                mListeners.add(WeakReference<OnFavoritePlayersChangeListener>(listener))
            }
        }
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        if (containsPlayer(player)) {
            mTimber.d(TAG, "Not adding favorite, it already exists in the store")
            return
        }

        mTimber.d(TAG, "Adding favorite (there are currently " + size() + " favorite(s))")

        val favoritePlayer = FavoritePlayer(player, region)
        val playerJson = mGson.toJson(favoritePlayer, FavoritePlayer::class.java)
        mKeyValueStore.setString(player.id, playerJson)
        notifyListeners()
    }

    override fun clear() {
        mKeyValueStore.clear()
        notifyListeners()
    }

    override fun containsPlayer(player: AbsPlayer): Boolean {
        return containsPlayer(player.id)
    }

    override fun containsPlayer(playerId: String): Boolean {
        return playerId in mKeyValueStore
    }

    override val isEmpty: Boolean
        get() {
            val all = mKeyValueStore.all
            return all == null || all.isEmpty()
        }

    private fun notifyListeners() {
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

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
            val all = mKeyValueStore.all

            if (all == null || all.isEmpty()) {
                return null
            }

            val players = mutableListOf<FavoritePlayer>()

            for ((_, value) in all) {
                val json = value as String
                players.add(mGson.fromJson(json, FavoritePlayer::class.java))
            }

            Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
            return players
        }

    override fun removeListener(listener: OnFavoritePlayersChangeListener?) {
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null || item === listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun removePlayer(player: AbsPlayer) {
        removePlayer(player.id)
    }

    override fun removePlayer(playerId: String) {
        mKeyValueStore.remove(playerId)
        notifyListeners()
    }

    override fun showAddOrRemovePlayerDialog(context: Context, player: AbsPlayer?, region: Region): Boolean {
        if (player == null) {
            return false
        }

        val builder = AlertDialog.Builder(context)
                .setNegativeButton(R.string.cancel, null)

        if (containsPlayer(player)) {
            builder.setMessage(context.getString(R.string.remove_x_from_favorites, player.name))
                    .setPositiveButton(R.string.yes) { dialog, which -> removePlayer(player) }
        } else {
            builder.setMessage(context.getString(R.string.add_x_to_favorites, player.name))
                    .setPositiveButton(R.string.yes) { dialog, which -> addPlayer(player, region) }
        }

        builder.show()
        return true
    }

    override fun size(): Int {
        val all = mKeyValueStore.all
        return all?.size ?: 0
    }

}
