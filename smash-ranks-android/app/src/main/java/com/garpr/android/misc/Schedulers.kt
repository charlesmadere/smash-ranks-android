package com.garpr.android.misc

import io.reactivex.Scheduler as RxScheduler

interface Schedulers {

    val background: RxScheduler

}
