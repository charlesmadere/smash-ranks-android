package com.garpr.android.extensions

import retrofit2.HttpException
import java.net.HttpRetryException

val Throwable?.httpCode: Int?
    get() {
        return when (this) {
            is HttpException -> code()
            is HttpRetryException -> responseCode()
            else -> null
        }
    }
