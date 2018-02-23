package com.garpr.android.misc

import com.garpr.android.misc.IdentityManager.OnIdentityChangeListener
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region
import com.garpr.android.preferences.Preference
import java.lang.ref.WeakReference

class IdentityManagerImpl(
        private val identityPreference: Preference<FavoritePlayer>,
        private val timber: Timber
) : IdentityManager {

    private val listeners = mutableListOf<WeakReference<OnIdentityChangeListener>>()


    companion object {
        private const val TAG = "IdentityManagerImpl"
    }

    override fun addListener(listener: OnIdentityChangeListener) {
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
        synchronized (listeners) {
            val iterator = listeners.iterator()

            while (iterator.hasNext()) {
                val reference = iterator.next()
                val item = reference.get()

                if (item == null) {
                    iterator.remove()
                } else {
                    item.onIdentityChange(this)
                }
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

    override fun setIdentity(player: AbsPlayer, region: Region) {
        timber.d(TAG, "old identity was \"" + getPlayerString(identity) + "\"" +
                ", new identity is \"" + getPlayerString(player) + "\"")

        identityPreference.set(FavoritePlayer(player.id, player.name, region))
        notifyListeners()
    }

}
