package com.garpr.android.data.models

/**
 * An Optional class was added to the Android framework itself in 7.0 (API 24). But unfortunately,
 * out minSdkVersion is currently 21 so we can't use that class. So in the meantime we can use
 * this class, which was designed to have an exactly identical API to the framework version.
 *
 * Whenever we switch to the framework version, this class should be just deleted entirely.
 */
class Optional<T : Any> private constructor(
        private val item: T?
) {

    fun get(): T {
        return item ?: throw NoSuchElementException()
    }

    fun isPresent(): Boolean {
        return item != null
    }

    fun orElse(other: T): T {
        return item ?: other
    }

    fun orNull(): T? {
        return item
    }

    companion object {
        private val EMPTY = Optional(null)

        fun <T : Any> empty(): Optional<T> {
            @Suppress("UNCHECKED_CAST")
            return EMPTY as Optional<T>
        }

        fun <T : Any> of(item: T): Optional<T> {
            return Optional(item)
        }

        fun <T : Any> ofNullable(item: T?): Optional<T> {
            return if (item == null) {
                empty()
            } else {
                Optional(item)
            }
        }
    }

}
