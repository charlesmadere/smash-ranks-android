package com.garpr.android.data.database

import androidx.room.TypeConverter
import com.garpr.android.data.models.Endpoint

class EndpointConverter {

    @TypeConverter
    fun endpointFromInt(int: Int): Endpoint {
        return Endpoint.values()[int]
    }

    @TypeConverter
    fun intFromEndpoint(endpoint: Endpoint): Int {
        return endpoint.ordinal
    }

}
