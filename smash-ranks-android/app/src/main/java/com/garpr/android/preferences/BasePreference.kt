package com.garpr.android.preferences

import com.garpr.android.data.models.Optional
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BasePreference<T : Any>(
        override val key: String,
        protected val defaultValue: T?
) : Preference<T> {

    private val subject = PublishSubject.create<Optional<T>>()

    override val observable: Observable<Optional<T>> = subject.hide()

    final override fun delete() {
        performDelete()
        subject.onNext(Optional.ofNullable(defaultValue))
    }

    protected abstract fun performDelete()

    protected abstract fun performSet(newValue: T)

    final override fun set(newValue: T?) {
        if (newValue == null) {
            delete()
        } else {
            performSet(newValue)
        }

        subject.onNext(Optional.ofNullable(newValue))
    }

}
