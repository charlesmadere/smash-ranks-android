package com.garpr.android.repositories

import android.annotation.SuppressLint
import androidx.annotation.WorkerThread
import com.garpr.android.data.database.DbFavoritePlayer
import com.garpr.android.data.database.FavoritePlayerDao
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
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
        private val favoritePlayerDao: FavoritePlayerDao,
        private val keyValueStore: KeyValueStore,
        private val moshi: Moshi,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : FavoritePlayersRepository, Refreshable {

    private val sizeSubject = BehaviorSubject.create<Int>()
    override val sizeObservable: Observable<Int> = sizeSubject.hide()

    private val playersSubject = BehaviorSubject.create<List<FavoritePlayer>>()
    override val playersObservable: Observable<List<FavoritePlayer>> = playersSubject.hide()

    init {
        initListeners()
        refresh()
    }

    override fun addPlayer(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            timber.d(TAG, "adding favorite...")

            favoritePlayerDao.insert(DbFavoritePlayer(
                    player = player,
                    region = region
            ))

            loadPlayers()
        }
    }

    override fun clear() {
        threadUtils.background.submit {
            timber.d(TAG, "clearing favorites...")
            favoritePlayerDao.deleteAll()
            loadPlayers()
        }
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
        val players = favoritePlayerDao.getAll()
                .map { dbFavoritePlayer ->
                    dbFavoritePlayer.toFavoritePlayer()
                }

        playersSubject.onNext(if (players.isNullOrEmpty()) {
            emptyList()
        } else {
            Collections.sort(players, AbsPlayer.ALPHABETICAL_ORDER)
            Collections.unmodifiableList(players)
        })
    }

    override fun migrate() {
        threadUtils.background.submit {
            migrateFromKeyValueStoreToRoom()
        }
    }

    @WorkerThread
    private fun migrateFromKeyValueStoreToRoom() {
        timber.d(TAG, "migrating favorites from KeyValueStore to Room...")

        val all = keyValueStore.all

        if (all.isNullOrEmpty()) {
            timber.d(TAG, "user has no favorites to migrate")
            return
        }

        val jsonAdapter = moshi.adapter(FavoritePlayer::class.java)

        val players: List<DbFavoritePlayer> = all.mapNotNull { entry ->
            val json = entry.value as? String?

            val player: FavoritePlayer? = if (json.isNullOrBlank()) {
                null
            } else {
                jsonAdapter.fromJson(json)
            }

            if (player == null) {
                null
            } else {
                DbFavoritePlayer(player = player, region = player.region)
            }
        }

        keyValueStore.clear()
        favoritePlayerDao.insertAll(players)

        timber.d(TAG, "finished migration process")

        loadPlayers()
    }

    override fun refresh() {
        threadUtils.background.submit {
            loadPlayers()
        }
    }

    override fun removePlayer(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            timber.d(TAG, "removing favorite...")

            favoritePlayerDao.delete(DbFavoritePlayer(
                    player = player,
                    region = region
            ))

            loadPlayers()
        }
    }

    companion object {
        private const val TAG = "FavoritePlayersRepositoryImpl"
    }

}
