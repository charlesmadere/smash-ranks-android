package com.garpr.android.data.models

data class ServerResponse<T>(
        val body: T?,
        val isSuccessful: Boolean,
        val code: Int,
        val message: String? = null
)
