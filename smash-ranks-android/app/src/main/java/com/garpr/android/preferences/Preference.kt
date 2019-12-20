package com.garpr.android.preferences

import com.garpr.android.data.models.Optional
import io.reactivex.Observable

interface Preference<T : Any> {

    val exists: Boolean

    val observable: Observable<Optional<T>>

    val key: String

    fun delete()

    fun get(): T?

    fun set(newValue: T?)

}
