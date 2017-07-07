package com.garpr.android.misc

import android.util.Log
import com.garpr.android.misc.Timber.Entry
import java.util.*

class TimberImpl(
        isLowRamDevice: Boolean,
        private val mCrashlyticsWrapper: CrashlyticsWrapper
) : Timber {

    private val mMaxSize: Int
    private val mEntries: MutableList<Timber.Entry>


    init {
        mMaxSize = if (isLowRamDevice) 64 else 256
        mEntries = ArrayList<Entry>(mMaxSize)
    }

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
        @Synchronized get() {
            return ArrayList<Entry>(mEntries)
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
