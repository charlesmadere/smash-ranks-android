package com.garpr.android.extensions

import io.reactivex.subjects.BehaviorSubject

fun <T : Any> BehaviorSubject<T?>.requireValue(): T {
    return checkNotNull(value)
}
