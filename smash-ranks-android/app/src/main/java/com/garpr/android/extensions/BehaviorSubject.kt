package com.garpr.android.extensions

import io.reactivex.subjects.BehaviorSubject

fun <T> BehaviorSubject<T>.requireValue(): T {
    return value ?: throw NullPointerException()
}
