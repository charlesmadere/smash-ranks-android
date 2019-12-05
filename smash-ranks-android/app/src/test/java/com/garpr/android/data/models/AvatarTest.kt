package com.garpr.android.data.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AvatarTest : BaseTest() {

    companion object {
        private val AVATAR_1 = Avatar(null, null, null, null)
        private val AVATAR_2 = Avatar("large", "medium", "original", "small")
        private val AVATAR_3 = Avatar(null, "medium", "original", "small")
        private val AVATAR_4 = Avatar(" ", null, "", "small")
        private val AVATAR_5 = Avatar("", " ", "original", null)
        private val AVATAR_6 = Avatar()
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

}
