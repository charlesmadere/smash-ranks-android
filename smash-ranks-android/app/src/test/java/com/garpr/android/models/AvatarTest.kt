package com.garpr.android.models

import com.garpr.android.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AvatarTest : BaseTest() {

    companion object {
        private val AVATAR_1 = Avatar(null, null, null)
        private val AVATAR_2 = Avatar("large", "medium", "small")
        private val AVATAR_3 = Avatar("large", "", "small")
        private val AVATAR_4 = Avatar(" ", null, "small")
        private val AVATAR_5 = Avatar("", " ", null)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall1() {
        assertNull(AVATAR_1.mediumButFallbackToLargeThenSmall)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall2() {
        assertEquals("medium", AVATAR_2.mediumButFallbackToLargeThenSmall)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall3() {
        assertEquals("large", AVATAR_3.mediumButFallbackToLargeThenSmall)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall4() {
        assertEquals("small", AVATAR_4.mediumButFallbackToLargeThenSmall)
    }

    @Test
    fun testMediumButFallbackToLargeThenSmall5() {
        assertNull(AVATAR_5.mediumButFallbackToLargeThenSmall)
    }

}
