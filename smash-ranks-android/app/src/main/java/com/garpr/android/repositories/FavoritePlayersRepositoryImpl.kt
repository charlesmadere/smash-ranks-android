package com.garpr.android.repositories

import android.annotation.SuppressLint
import androidx.annotation.WorkerThread
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.requireFromJson
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.KeyValueStore
import com.squareup.moshi.Moshi
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.Collections

class FavoritePlayersRepositoryImpl(
        private val keyValueStore: KeyValueStore,
        private val moshi: Moshi,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : FavoritePlayersRepository, Refreshable {

    private val favoritePlayerJsonAdapter by lazy {
        moshi.adapter(FavoritePlayer::class.java)
    }

    private val sizeSubject = BehaviorSubject.create<Int>()
    override val sizeObservable: Observable<Int> = sizeSubject.hide()

    private val playersSubject = BehaviorSubject.create<List<FavoritePlayer>>()
    override val playersObservable: Observable<List<FavoritePlayer>> = playersSubject.hide()

    companion object {
        private const val TAG = "FavoritePlayersRepositoryImpl"
    }

    init {
        initListeners()
        refresh()
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            if (player in this) {
                timber.w(TAG, "Not adding favorite, it already exists in the store")
                return@submit
            }

            timber.d(TAG, "adding favorite...")

            val favoritePlayer = FavoritePlayer(player.id, player.name, region)
            val playerJson = favoritePlayerJsonAdapter.toJson(favoritePlayer)
            keyValueStore.setString(player.id, playerJson)

            loadPlayers()
        }
    }

    override fun clear() {
        threadUtils.background.submit {
            timber.d(TAG, "clearing favorites...")
            keyValueStore.clear()
            loadPlayers()
        }
    }

    override fun contains(player: AbsPlayer): Boolean {
        return player.id in keyValueStore
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        playersObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .map { players ->
                    players.size
                }
                .subscribe { size ->
                    sizeSubject.onNext(size)
                }
    }

    @WorkerThread
    private fun loadPlayers() {
        val all = keyValueStore.all

        if (all.isNullOrEmpty()) {
            playersSubject.onNext(emptyList())
            return
        }

        val players = all.map { entry ->
            val json = entry.value as String
            favoritePlayerJsonAdapter.requireFromJson(json)
        }

        Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
        playersSubject.onNext(players)
    }

    override fun refresh() {
        threadUtils.background.submit {
            loadPlayers()
        }
    }

    override fun removePlayer(player: AbsPlayer) {
        threadUtils.background.submit {
            timber.d(TAG, "removing favorite...")
            keyValueStore.remove(player.id)
            loadPlayers()
        }
    }

}
