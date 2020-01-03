package com.garpr.android.repositories

import android.annotation.SuppressLint
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Optional
import com.garpr.android.data.models.Region
import com.garpr.android.extensions.requireValue
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

    override val hasIdentity: Boolean
        get() = hasIdentitySubject.requireValue()

    override val identity: FavoritePlayer?
        get() = identitySubject.requireValue().item

    private val hasIdentitySubject = BehaviorSubject.create<Boolean>()
    override val hasIdentityObservable: Observable<Boolean> = hasIdentitySubject.hide()

    private val identitySubject = BehaviorSubject.create<Optional<FavoritePlayer>>()
    override val identityObservable: Observable<Optional<FavoritePlayer>> = identitySubject.hide()

    companion object {
        private const val TAG = "IdentityRepositoryImpl"
    }

    init {
        initListeners()
        refresh()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        identityObservable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    hasIdentitySubject.onNext(optional.isPresent)
                }

        generalPreferenceStore.identity.observable
                .subscribeOn(schedulers.background)
                .observeOn(schedulers.background)
                .subscribe { optional ->
                    identitySubject.onNext(optional)
                }
    }

    override fun isPlayer(player: AbsPlayer?): Boolean {
        return player != null && identity == player
    }

    override fun refresh() {
        threadUtils.background.submit {
            val identity = generalPreferenceStore.identity.get()
            val optional = Optional.ofNullable(identity)
            identitySubject.onNext(optional)
        }
    }

    override fun removeIdentity() {
        threadUtils.background.submit {
            timber.d(TAG, "removing identity...")
            generalPreferenceStore.identity.delete()
        }
    }

    override fun setIdentity(player: AbsPlayer, region: Region) {
        threadUtils.background.submit {
            timber.d(TAG, "setting identity...")

            val newIdentity = FavoritePlayer(
                    id = player.id,
                    name = player.name,
                    region = region
            )

            generalPreferenceStore.identity.set(newIdentity)
        }
    }

}
