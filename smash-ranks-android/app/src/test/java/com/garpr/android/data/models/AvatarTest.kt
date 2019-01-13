package com.garpr.android.data.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class AvatarTest : BaseTest() {

    @Inject
    protected lateinit var gson: Gson


    companion object {
        private val AVATAR_1 = Avatar(null, null, null, null)
        private val AVATAR_2 = Avatar("large", "medium", "original", "small")
        private val AVATAR_3 = Avatar(null, "medium", "original", "small")
        private val AVATAR_4 = Avatar(" ", null, "", "small")
        private val AVATAR_5 = Avatar("", " ", "original", null)
        private val AVATAR_6 = Avatar()

        private const val JSON_AVATAR_1 = "{\"original\":\"abc.jpg\"}"
        private const val JSON_AVATAR_2 = "{\"small\":\"s.jpg\"}"
        private const val JSON_AVATAR_3 = "{\"large\":\"l.jpg\",\"medium\":\"m.jpg\"}"
    }

    @Before
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    fun testFromJsonAvatar1() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_1, Avatar::class.java)

        assertNotNull(avatar)
        assertNull(avatar.large)
        assertNull(avatar.medium)
        assertNotNull(avatar.original)
        assertNull(avatar.small)

        assertNotNull(avatar.largeButFallbackToMediumThenOriginalThenSmall)
        assertEquals(avatar.original, avatar.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testFromJsonAvatar2() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_2, Avatar::class.java)

        assertNotNull(avatar)
        assertNull(avatar.large)
        assertNull(avatar.medium)
        assertNull(avatar.original)
        assertNotNull(avatar.small)

        assertNotNull(avatar.largeButFallbackToMediumThenOriginalThenSmall)
        assertEquals(avatar.small, avatar.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testFromJsonAvatar3() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_3, Avatar::class.java)

        assertNotNull(avatar)
        assertNotNull(avatar.large)
        assertNotNull(avatar.medium)
        assertNull(avatar.original)
        assertNull(avatar.small)

        assertNotNull(avatar.largeButFallbackToMediumThenOriginalThenSmall)
        assertEquals(avatar.large, avatar.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testFromJsonEmptyString() {
        assertNull(gson.fromJson("", Avatar::class.java))
    }

    @Test
    fun testFromJsonNullJsonElement() {
        assertNull(gson.fromJson(null as JsonElement?, Avatar::class.java))
    }

    @Test
    fun testFromJsonNullString() {
        assertNull(gson.fromJson(null as String?, Avatar::class.java))
    }

    @Test
    fun testMediumButFallbackToNull1() {
        assertNull(AVATAR_1.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToNull2() {
        assertNull(AVATAR_6.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testLargeButFallbackToLarge() {
        assertEquals("large", AVATAR_2.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testLargeButFallbackToMedium() {
        assertEquals("medium", AVATAR_3.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testLargeButFallbackToSmall() {
        assertEquals("small", AVATAR_4.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testLargeButFallbackToOriginal() {
        assertEquals("original", AVATAR_5.largeButFallbackToMediumThenOriginalThenSmall)
    }

    @Test
    fun testToJsonFromAvatar1() {
        val avatar1: Avatar = gson.fromJson(JSON_AVATAR_1, Avatar::class.java)
        val json = gson.toJson(avatar1)
        assertEquals("{\"original\":\"abc.jpg\"}", json)

        val avatar2: Avatar = gson.fromJson(json, Avatar::class.java)
        assertEquals(avatar1, avatar2)
    }

    @Test
    fun testToJsonFromAvatar2() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_2, Avatar::class.java)
        val json = gson.toJson(avatar)

        val avatar2: Avatar = gson.fromJson(json, Avatar::class.java)
        assertEquals(avatar, avatar2)
    }

    @Test
    fun testToJsonFromAvatar3() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_3, Avatar::class.java)
        val json = gson.toJson(avatar)

        val avatar2: Avatar = gson.fromJson(json, Avatar::class.java)
        assertEquals(avatar, avatar2)
    }

}
