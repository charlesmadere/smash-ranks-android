package com.garpr.android.misc

import com.garpr.android.misc.IdentityManager.OnIdentityChangeListener
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region
import com.garpr.android.preferences.Preference
import java.lang.ref.WeakReference

class IdentityManagerImpl(
        private val mIdentity: Preference<FavoritePlayer>,
        private val mTimber: Timber
) : IdentityManager {

    private val mListeners = mutableListOf<WeakReference<OnIdentityChangeListener>>()


    companion object {
        private const val TAG = "IdentityManagerImpl"
    }

    override fun addListener(listener: OnIdentityChangeListener) {
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
                mListeners.add(WeakReference<OnIdentityChangeListener>(listener))
            }
        }
    }

    override val identity: FavoritePlayer?
        get() = mIdentity.get()

    private fun getPlayerString(player: AbsPlayer?): String {
        if (player == null) {
            return "null"
        } else {
            return "(id:" + player.id + ") (name:" + player.name + ")"
        }
    }

    override val hasIdentity: Boolean
        get() = mIdentity.exists

    override fun isPlayer(player: AbsPlayer?): Boolean {
        return identity?.let { it == player } ?: false
    }

    override fun isPlayer(id: String?): Boolean {
        return identity?.id?.equals(id, ignoreCase = true) ?: false
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
                    item.onIdentityChange(this)
                }
            }
        }
    }

    override fun removeIdentity() {
        mTimber.d(TAG, "identity is being removed, old identity was \"" +
                getPlayerString(identity) + "\"")

        mIdentity.delete()
        notifyListeners()
    }

    override fun removeListener(listener: OnIdentityChangeListener?) {
        synchronized (mListeners) {
            val iterator = mListeners.iterator()

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
        mTimber.d(TAG, "old identity was \"" + getPlayerString(identity) + "\"" +
                ", new identity is \"" + getPlayerString(player) + "\"")

        mIdentity.set(FavoritePlayer(player.id, player.name, region))
        notifyListeners()
    }

}
