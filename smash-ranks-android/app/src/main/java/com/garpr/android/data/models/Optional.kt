package com.garpr.android.data.models

class Optional<T : Any> private constructor(
        private val item: T?
) {

    fun get(): T {
        return checkNotNull(item)
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
