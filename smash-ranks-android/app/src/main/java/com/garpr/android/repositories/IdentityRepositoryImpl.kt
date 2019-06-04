package com.garpr.android.repositories

import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.misc.Timber
import com.garpr.android.preferences.Preference
import com.garpr.android.repositories.IdentityRepository.OnIdentityChangeListener
import com.garpr.android.wrappers.WeakReferenceWrapper

class IdentityRepositoryImpl(
        private val identityPreference: Preference<FavoritePlayer>,
        private val timber: Timber
) : IdentityRepository {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnIdentityChangeListener>>()


    companion object {
        private const val TAG = "IdentityRepositoryImpl"
    }

    override fun addListener(listener: OnIdentityChangeListener) {
        cleanListeners()

        synchronized (listeners) {
            listeners.add(WeakReferenceWrapper(listener))
        }
    }

    private fun cleanListeners(listenerToRemove: OnIdentityChangeListener? = null) {
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

    override val identity: FavoritePlayer?
        get() = identityPreference.get()

    override val hasIdentity: Boolean
        get() = identityPreference.exists

    override fun isPlayer(player: AbsPlayer?): Boolean {
        return player != null && identity == player
    }

    override fun isPlayer(id: String?): Boolean {
        return identity?.id?.equals(id, ignoreCase = true) == true
    }

    private fun notifyListeners() {
        cleanListeners()

        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                iterator.next().get()?.onIdentityChange(this)
            }
        }
    }

    override fun removeIdentity() {
        timber.d(TAG, "identity is being removed, hasIdentity: $hasIdentity")
        identityPreference.delete()
        notifyListeners()
    }

    override fun removeListener(listener: OnIdentityChangeListener?) {
        cleanListeners(listener)
    }

    override fun setIdentity(player: AbsPlayer, region: Region) {
        timber.d(TAG, "identity is being set, hasIdentity: $hasIdentity")
        identityPreference.set(FavoritePlayer(player.id, player.name, region))
        notifyListeners()
    }

}
