package com.garpr.android.managers

import com.garpr.android.managers.IdentityManager.OnIdentityChangeListener
import com.garpr.android.misc.Timber
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region
import com.garpr.android.preferences.Preference
import com.garpr.android.wrappers.WeakReferenceWrapper

class IdentityManagerImpl(
        private val identityPreference: Preference<FavoritePlayer>,
        private val timber: Timber
) : IdentityManager {

    private val listeners = mutableSetOf<WeakReferenceWrapper<OnIdentityChangeListener>>()


    companion object {
        private const val TAG = "IdentityManagerImpl"
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

    private fun getPlayerString(player: AbsPlayer?): String {
        return if (player == null) { "null" } else { "(id:${player.id}) (name:${player.name})" }
    }

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
        timber.d(TAG, "identity is being removed, old identity was \"" +
                getPlayerString(identity) + "\"")

        identityPreference.delete()
        notifyListeners()
    }

    override fun removeListener(listener: OnIdentityChangeListener?) {
        cleanListeners(listener)
    }

    override fun setIdentity(player: AbsPlayer, region: Region) {
        timber.d(TAG, "old identity was \"" + getPlayerString(identity) + "\"" +
                ", new identity is \"" + getPlayerString(player) + "\"")

        identityPreference.set(FavoritePlayer(player.id, player.name, region))
        notifyListeners()
    }

}
