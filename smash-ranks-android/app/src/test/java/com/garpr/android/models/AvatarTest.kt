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
        private val AVATAR_2 = Avatar("large", "medium", "other", "small")
        private val AVATAR_3 = Avatar("large", "", "other", "small")
        private val AVATAR_4 = Avatar(" ", null, "", "small")
        private val AVATAR_5 = Avatar("", " ", "other", null)
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
        assertNotNull(avatar.mediumButFallbackToLargeThenSmallThenOther)
        assertNotNull(avatar.other)
        assertNull(avatar.small)
    }

    @Test
    fun testFromJsonAvatar2() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_2, Avatar::class.java)
        assertNotNull(avatar)
        assertNull(avatar.large)
        assertNull(avatar.medium)
        assertNotNull(avatar.mediumButFallbackToLargeThenSmallThenOther)
        assertNull(avatar.other)
        assertNotNull(avatar.small)
    }

    @Test
    fun testFromJsonAvatar3() {
        val avatar: Avatar = gson.fromJson(JSON_AVATAR_3, Avatar::class.java)
        assertNotNull(avatar)
        assertNotNull(avatar.large)
        assertNotNull(avatar.medium)
        assertNotNull(avatar.mediumButFallbackToLargeThenSmallThenOther)
        assertNull(avatar.other)
        assertNull(avatar.small)
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
    fun testMediumButFallbackToLargeThenSmallThenOther1() {
        assertNull(AVATAR_1.mediumButFallbackToLargeThenSmallThenOther)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmallThenOther2() {
        assertEquals("medium", AVATAR_2.mediumButFallbackToLargeThenSmallThenOther)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmallThenOther3() {
        assertEquals("large", AVATAR_3.mediumButFallbackToLargeThenSmallThenOther)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmallThenOther4() {
        assertEquals("small", AVATAR_4.mediumButFallbackToLargeThenSmallThenOther)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall5ThenOther5() {
        assertEquals("other", AVATAR_5.mediumButFallbackToLargeThenSmallThenOther)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmallThenOther6() {
        assertNull(AVATAR_6.mediumButFallbackToLargeThenSmallThenOther)
    }

}
