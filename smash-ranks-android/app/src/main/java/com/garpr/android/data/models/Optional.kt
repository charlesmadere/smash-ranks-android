package com.garpr.android.data.models

class Optional<T : Any> private constructor(
        val item: T?
) {

    val isPresent: Boolean = item != null

    fun orElse(other: T): T {
        return item ?: other
    }

    fun require(): T {
        return checkNotNull(item)
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
