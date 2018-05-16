package com.garpr.android.models

import com.garpr.android.BaseTest
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.junit.Assert.*
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
        private val AVATAR_3 = Avatar("large", "", "original", "small")
        private val AVATAR_4 = Avatar(" ", null, "", "small")
        private val AVATAR_5 = Avatar("", " ", "original", null)
        private val AVATAR_6 = Avatar()

        private const val JSON_AVATAR_1 = "\"adc.jpg\""
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

        assertNotNull(avatar.mediumButFallbackToLargeThenOriginalThenSmall)
        assertEquals(avatar.original, avatar.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testFromJsonAvatar2() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_2, Avatar::class.java)

        assertNotNull(avatar)
        assertNull(avatar.large)
        assertNull(avatar.medium)
        assertNull(avatar.original)
        assertNotNull(avatar.small)

        assertNotNull(avatar.mediumButFallbackToLargeThenOriginalThenSmall)
        assertEquals(avatar.small, avatar.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testFromJsonAvatar3() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_3, Avatar::class.java)

        assertNotNull(avatar)
        assertNotNull(avatar.large)
        assertNotNull(avatar.medium)
        assertNull(avatar.original)
        assertNull(avatar.small)

        assertNotNull(avatar.mediumButFallbackToLargeThenOriginalThenSmall)
        assertEquals(avatar.medium, avatar.mediumButFallbackToLargeThenOriginalThenSmall)
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
        assertNull(AVATAR_1.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToNull2() {
        assertNull(AVATAR_6.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToMedium() {
        assertEquals("medium", AVATAR_2.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToLarge() {
        assertEquals("large", AVATAR_3.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToSmall() {
        assertEquals("small", AVATAR_4.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testMediumButFallbackToOriginal() {
        assertEquals("original", AVATAR_5.mediumButFallbackToLargeThenOriginalThenSmall)
    }

    @Test
    fun testToJsonFromAvatar1() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_1, Avatar::class.java)
        val json = gson.toJson(avatar)

        val avatar2: Avatar = gson.fromJson(json, Avatar::class.java)
        assertEquals(avatar, avatar2)
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
