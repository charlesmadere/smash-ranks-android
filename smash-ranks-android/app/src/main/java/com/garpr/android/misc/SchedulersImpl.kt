package com.garpr.android.misc

import io.reactivex.Scheduler as RxScheduler
import io.reactivex.schedulers.Schedulers as RxSchedulers

class SchedulersImpl(
        private val threadUtils: ThreadUtils
) : Schedulers {

    override val background: RxScheduler by lazy { RxSchedulers.from(threadUtils.background) }

}
