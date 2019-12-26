package com.garpr.android.repositories

import android.annotation.SuppressLint
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Refreshable
import com.garpr.android.misc.Schedulers
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.GeneralPreferenceStore
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class IdentityRepositoryImpl(
        private val generalPreferenceStore: GeneralPreferenceStore,
        private val schedulers: Schedulers,
        private val threadUtils: ThreadUtils,
        private val timber: Timber
) : IdentityRepository, Refreshable {

    companion object {
        private const val TAG = "IdentityRepositoryImpl"
    }

    override val hasIdentity: Boolean
        get() = identity != null

    override val identity: FavoritePlayer?
        get() = identitySubject.value?.item

    private val identitySubject = BehaviorSubject.create<Optional<FavoritePlayer>>()
    override val identityObservable: Observable<Optional<FavoritePlayer>> = identitySubject.hide()

    init {
        initListeners()
        refresh()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        generalPreferenceStore.identity.observable
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    val identity = optional.item
                    identitySubject.onNext(Optional.ofNullable(identity))
                }
    }

    override fun isPlayer(player: AbsPlayer?): Boolean {
        return player != null && identity == player
    }

    override fun isPlayer(id: String?): Boolean {
        return identity?.id?.equals(id, ignoreCase = true) == true
    }

    override fun refresh() {
        threadUtils.background.submit {
            val identity = generalPreferenceStore.identity.get()
            identitySubject.onNext(Optional.ofNullable(identity))
        }
    }

    override fun removeIdentity() {
        threadUtils.background.submit {
            timber.d(TAG, "identity is being removed, hasIdentity: $hasIdentity")
            generalPreferenceStore.identity.delete()
        }
    }

    override fun setIdentity(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            timber.d(TAG, "identity is being set, hasIdentity: $hasIdentity")

            val newIdentity = FavoritePlayer(
                    id = player.id,
                    name = player.name,
                    region = region
            )

            generalPreferenceStore.identity.set(newIdentity)
        }
    }

}
