package com.garpr.android.extensions

import io.reactivex.Observable
import io.reactivex.Single

fun <T> Observable<T>.takeSingle(): Single<T> {
    return take(1).singleOrError()
}
