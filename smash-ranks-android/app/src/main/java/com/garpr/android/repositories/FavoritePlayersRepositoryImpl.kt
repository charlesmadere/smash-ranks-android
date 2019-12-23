package com.garpr.android.repositories

import androidx.annotation.WorkerThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.requireFromJson
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.KeyValueStore
import com.squareup.moshi.JsonAdapter
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.Collections

class FavoritePlayersRepositoryImpl(
        private val favoritePlayerAdapter: JsonAdapter<FavoritePlayer>,
        private val keyValueStore: KeyValueStore,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : FavoritePlayersRepository, Refreshable {

    override val isEmpty: Boolean
        get() = size == 0

    override val size: Int
        get() = playersSubject.value?.item?.size ?: 0

    override val players: List<FavoritePlayer>?
        get() = playersSubject.value?.item

    private val playersSubject = BehaviorSubject.createDefault<Optional<List<FavoritePlayer>>>(Optional.empty())
    override val playersObservable: Observable<Optional<List<FavoritePlayer>>> = playersSubject.hide()

    companion object {
        private const val TAG = "FavoritePlayersRepositoryImpl"
    }

    init {
        refresh()
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            if (player in this) {
                timber.d(TAG, "Not adding favorite, it already exists in the store")
                return@submit
            }

            timber.d(TAG, "Adding favorite (there are currently $size)")

            val favoritePlayer = FavoritePlayer(player.id, player.name, region)
            val playerJson = favoritePlayerAdapter.toJson(favoritePlayer)
            keyValueStore.setString(player.id, playerJson)

            loadPlayers()
        }
    }

    override fun clear() {
        threadUtils.background.submit {
            timber.d(TAG, "Clearing favorites (there are currently $size)")
            keyValueStore.clear()
            loadPlayers()
        }
    }

    override fun contains(player: AbsPlayer): Boolean {
        return player.id in this
    }

    override fun contains(playerId: String): Boolean {
        return playerId in keyValueStore
    }

    @WorkerThread
    private fun loadPlayers() {
        val all = keyValueStore.all

        if (all.isNullOrEmpty()) {
            playersSubject.onNext(Optional.empty())
            return
        }

        val players = all.map { entry ->
            val json = entry.value as String
            favoritePlayerAdapter.requireFromJson(json)
        }

        Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
        playersSubject.onNext(Optional.of(players))
    }

    override fun refresh() {
        threadUtils.background.submit {
            loadPlayers()
        }
    }

    override fun removePlayer(player: AbsPlayer) {
        removePlayer(player.id)
    }

    override fun removePlayer(playerId: String) {
        threadUtils.background.submit {
            timber.d(TAG, "Removing favorite (there are currently $size)")
            keyValueStore.remove(playerId)
            loadPlayers()
        }
    }

}
