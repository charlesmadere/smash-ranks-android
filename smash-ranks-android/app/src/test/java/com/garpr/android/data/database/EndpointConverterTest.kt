package com.garpr.android.data.database

import com.garpr.android.data.models.Endpoint
import com.garpr.android.test.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EndpointConverterTest : BaseTest() {

    private val converter = EndpointConverter()

    @Test
    fun testEndpointFromIntWithGarPr() {
        val endpoint = converter.endpointFromInt(Endpoint.GAR_PR.ordinal)
        assertEquals(Endpoint.GAR_PR, endpoint)
    }

    @Test
    fun testEndpointFromIntWithNotGarPr() {
        val endpoint = converter.endpointFromInt(Endpoint.NOT_GAR_PR.ordinal)
        assertEquals(Endpoint.NOT_GAR_PR, endpoint)
    }

    @Test
    fun testIntFromEndpointWithGarPr() {
        val int = converter.intFromEndpoint(Endpoint.GAR_PR)
        assertEquals(Endpoint.GAR_PR.ordinal, int)
    }

    @Test
    fun testIntFromEndpointWithNotGarPr() {
        val int = converter.intFromEndpoint(Endpoint.NOT_GAR_PR)
        assertEquals(Endpoint.NOT_GAR_PR.ordinal, int)
    }

}
