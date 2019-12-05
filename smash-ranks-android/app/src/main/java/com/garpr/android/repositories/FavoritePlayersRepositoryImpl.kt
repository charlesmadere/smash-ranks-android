package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.requireFromJson
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.KeyValueStore
import com.garpr.android.repositories.FavoritePlayersRepository.OnFavoritePlayersChangeListener
import com.garpr.android.wrappers.WeakReferenceWrapper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.util.Collections

class FavoritePlayersRepositoryImpl(
        private val keyValueStore: KeyValueStore,
        private val moshi: Moshi,
        private val timber: Timber
) : FavoritePlayersRepository {

    private val favoritePlayerAdapter: JsonAdapter<FavoritePlayer> by lazy {
        moshi.adapter(FavoritePlayer::class.java)
    }

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnFavoritePlayersChangeListener>>()

    companion object {
        private const val TAG = "FavoritePlayersRepositoryImpl"
    }

    override fun addListener(listener: OnFavoritePlayersChangeListener) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        if (player in this) {
            timber.d(TAG, "Not adding favorite, it already exists in the store")
            return
        }

        timber.d(TAG, "Adding favorite (there are currently $size)")

        val favoritePlayer = FavoritePlayer(player.id, player.name, region)
        val playerJson = favoritePlayerAdapter.toJson(favoritePlayer)
        keyValueStore.setString(player.id, playerJson)
        notifyListeners()
    }

    private fun cleanListeners(listenerToRemove: OnFavoritePlayersChangeListener? = null) {
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val listener = iterator.next().get()

                if (listener == null || listener == listenerToRemove) {
                    iterator.remove()
                }
            }
        }
    }

    override fun clear() {
        timber.d(TAG, "Clearing favorites (there are currently $size)")
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
        get() = keyValueStore.all.isNullOrEmpty()

    private fun notifyListeners() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onFavoritePlayersChange(this)
            }
        }
    }

    override val players: List<FavoritePlayer>?
        get() {
            val all = keyValueStore.all

            if (all.isNullOrEmpty()) {
                return null
            }

            val players = mutableListOf<FavoritePlayer>()

            for ((key, value) in all) {
                val json = value as String
                val player = favoritePlayerAdapter.requireFromJson(json)
                players.add(player)
            }

            Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
            return players
        }

    override fun removeListener(listener: OnFavoritePlayersChangeListener?) {
        cleanListeners(listener)
    }

    override fun removePlayer(player: AbsPlayer) {
        removePlayer(player.id)
    }

    override fun removePlayer(playerId: String) {
        timber.d(TAG, "Removing favorite (there are currently $size)")
        keyValueStore.remove(playerId)
        notifyListeners()
    }

    override val size: Int
        get() = keyValueStore.all?.size ?: 0

}
