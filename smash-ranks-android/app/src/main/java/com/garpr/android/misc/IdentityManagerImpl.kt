package com.garpr.android.misc

import android.text.TextUtils
import com.garpr.android.misc.IdentityManager.OnIdentityChangeListener
import com.garpr.android.models.AbsPlayer
import com.garpr.android.models.FavoritePlayer
import com.garpr.android.models.Region
import com.garpr.android.preferences.Preference
import java.lang.ref.WeakReference
import java.util.*

class IdentityManagerImpl(
        private val mIdentity: Preference<FavoritePlayer>,
        private val mTimber: Timber
) : IdentityManager {

    private val mListeners: MutableList<WeakReference<OnIdentityChangeListener>> = LinkedList()


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
        get() {
            return mIdentity.get()
        }

    private fun getPlayerString(player: AbsPlayer?): String {
        if (player == null) {
            return "null"
        } else {
            return "(id:" + player.id + ") (name:" + player.name + ")"
        }
    }

    override fun hasIdentity(): Boolean {
        return mIdentity.exists()
    }

    override fun isId(id: String?): Boolean {
        if (TextUtils.isEmpty(id)) {
            return false
        }

        val identity = identity
        return identity != null && identity.id == id
    }

    override fun isPlayer(player: AbsPlayer?): Boolean {
        if (player == null) {
            return false
        }

        val identity = identity
        return identity != null && identity == player
    }

    private fun notifyListeners() {
        synchronized(mListeners) {
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

                if (item == null || item === listener) {
                    iterator.remove()
                }
            }
        }
    }

    override fun setIdentity(player: AbsPlayer, region: Region) {
        mTimber.d(TAG, "old identity was \"" + getPlayerString(identity) + "\"" +
                ", new identity is \"" + getPlayerString(player) + "\"")

        mIdentity.set(FavoritePlayer(player, region))
        notifyListeners()
    }

}
