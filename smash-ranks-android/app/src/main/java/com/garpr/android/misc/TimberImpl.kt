package com.garpr.android.misc

import android.util.Log
import com.garpr.android.wrappers.CrashlyticsWrapper
import java.util.Collections
import java.util.LinkedList

class TimberImpl(
        deviceUtils: DeviceUtils,
        private val crashlyticsWrapper: CrashlyticsWrapper
) : Timber {

    private val maxSize: Int = if (deviceUtils.hasLowRam) 64 else 256
    private val _entries: MutableList<Timber.Entry> = LinkedList()


    private fun addEntry(entry: Timber.Entry) {
        synchronized (_entries) {
            if (_entries.size >= maxSize) {
                _entries.removeAt(_entries.size - 1)
            }

            _entries.add(0, entry)
        }
    }

    override fun clearEntries() {
        synchronized (_entries) {
            _entries.clear()
        }
    }

    override fun d(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.DebugEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.DEBUG, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.ErrorEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.ERROR, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

    override val entries: List<Timber.Entry>
        get() {
            val list = mutableListOf<Timber.Entry>()

            synchronized (_entries) {
                list.addAll(_entries)
            }

            return Collections.unmodifiableList(list)
        }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.WarnEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.WARN, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

}
