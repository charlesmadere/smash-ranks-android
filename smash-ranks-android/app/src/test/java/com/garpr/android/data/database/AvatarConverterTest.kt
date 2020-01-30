package com.garpr.android.data.database

import com.garpr.android.data.models.Avatar
import com.garpr.android.test.BaseTest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

class AvatarConverterTest : BaseTest() {

    private val converter = AvatarConverter()
    private lateinit var jsonAdapter: JsonAdapter<Avatar>

    protected val moshi: Moshi by inject()

    @Before
    override fun setUp() {
        super.setUp()

        jsonAdapter = moshi.adapter(Avatar::class.java)
    }

    @Test
    fun testAvatarFromStringWithAll() {
        val avatar = converter.avatarFromString(ALL_JSON)
        assertNotNull(avatar)
        assertEquals(ALL, avatar)
    }

    @Test
    fun testAvatarFromStringWithEmptyString() {
        assertNull(converter.avatarFromString(""))
    }

    @Test
    fun testAvatarFromStringWithNull() {
        assertNull(converter.avatarFromString(null))
    }

    @Test
    fun testAvatarFromStringWithOriginalOnly() {
        val avatar = converter.avatarFromString(ORIGINAL_ONLY_JSON)
        assertNotNull(avatar)
        assertEquals(ORIGINAL_ONLY, avatar)
    }

    @Test
    fun testStringFromAvatarWithAll() {
        val string = converter.stringFromAvatar(ALL)
        assertFalse(string.isNullOrBlank())

        val avatar = jsonAdapter.fromJson(string!!)
        assertEquals(ALL, avatar)
    }

    @Test
    fun testStringFromAvatarWithOriginalOnly() {
        val string = converter.stringFromAvatar(ORIGINAL_ONLY)
        assertFalse(string.isNullOrBlank())

        val avatar = jsonAdapter.fromJson(string!!)
        assertEquals(ORIGINAL_ONLY, avatar)
    }

    @Test
    fun testStringFromAvatarWithNull() {
        assertNull(converter.stringFromAvatar(null))
    }

    companion object {
        private val ALL = Avatar(
                large = "large.jpg",
                medium = "medium.jpg",
                original = "original.jpg",
                small = "small.jpg"
        )

        private val ORIGINAL_ONLY = Avatar(
                original = "hello_world.jpg"
        )

        private val ALL_JSON = "{\"large\":\"${ALL.large}\",\"medium\":\"${ALL.medium}\",\"original\":\"${ALL.original}\",\"small\":\"${ALL.small}\"}"
        private val ORIGINAL_ONLY_JSON = "{\"original\":\"${ORIGINAL_ONLY.original}\"}"
    }

}
