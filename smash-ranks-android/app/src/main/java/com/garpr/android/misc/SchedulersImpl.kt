package com.garpr.android.misc

import io.reactivex.Scheduler as RxScheduler
import io.reactivex.schedulers.Schedulers as RxSchedulers

class SchedulersImpl(
        private val threadUtils2: ThreadUtils2
) : Schedulers {

    override val background: RxScheduler by lazy { RxSchedulers.from(threadUtils2.background) }

}
