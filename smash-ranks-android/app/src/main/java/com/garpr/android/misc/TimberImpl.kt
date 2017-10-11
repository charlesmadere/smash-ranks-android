package com.garpr.android.misc

import android.util.Log

class TimberImpl(
        isLowRamDevice: Boolean,
        private val mCrashlyticsWrapper: CrashlyticsWrapper
) : Timber {

    private val mMaxSize: Int = if (isLowRamDevice) 64 else 256
    private val mEntries = mutableListOf<Timber.Entry>()


    private fun addEntry(entry: Timber.Entry) {
        if (mEntries.size >= mMaxSize) {
            mEntries.removeAt(mEntries.size - 1)
        }

        mEntries.add(0, entry)
    }

    @Synchronized override fun clearEntries() {
        mEntries.clear()
    }

    @Synchronized override fun d(tag: String, msg: String) {
        d(tag, msg, null)
    }

    @Synchronized override fun d(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.DebugEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        mCrashlyticsWrapper.log(Log.DEBUG, tag, msg)

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr)
        }
    }

    @Synchronized override fun e(tag: String, msg: String) {
        e(tag, msg, null)
    }

    @Synchronized override fun e(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.ErrorEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        mCrashlyticsWrapper.log(Log.ERROR, tag, msg)

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr)
        }
    }

    override val entries: List<Timber.Entry>
        get() {
            val list = mutableListOf<Timber.Entry>()

            synchronized (this) {
                list.addAll(mEntries)
            }

            return list
        }

    @Synchronized override fun w(tag: String, msg: String) {
        w(tag, msg, null)
    }

    @Synchronized override fun w(tag: String, msg: String, tr: Throwable?) {
        addEntry(Timber.WarnEntry(tag, msg, if (tr == null) null else Log.getStackTraceString(tr)))
        mCrashlyticsWrapper.log(Log.WARN, tag, msg)

        if (tr != null) {
            mCrashlyticsWrapper.logException(tr)
        }
    }

}
