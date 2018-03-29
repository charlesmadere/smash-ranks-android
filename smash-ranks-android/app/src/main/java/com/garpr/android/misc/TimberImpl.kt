package com.garpr.android.misc

import android.util.Log

class TimberImpl(
        isLowRamDevice: Boolean,
        private val crashlyticsWrapper: CrashlyticsWrapper
) : Timber {

    private val maxSize: Int = if (isLowRamDevice) 64 else 256
    private val _entries = mutableListOf<Timber.Entry>()


    private fun addEntry(entry: Timber.Entry) {
        if (_entries.size >= maxSize) {
            _entries.removeAt(_entries.size - 1)
        }

        _entries.add(0, entry)
    }

    @Synchronized override fun clearEntries() {
        _entries.clear()
    }

    @Synchronized override fun d(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.DebugEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.DEBUG, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

    @Synchronized override fun e(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.ErrorEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.ERROR, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

    override val entries: List<Timber.Entry>
        get() {
            val list = mutableListOf<Timber.Entry>()

            synchronized (this) {
                list.addAll(_entries)
            }

            return list
        }

    @Synchronized override fun w(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.WarnEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        crashlyticsWrapper.log(Log.WARN, tag, msg)

        if (tr != null) {
            crashlyticsWrapper.logException(tr)
        }
    }

}
