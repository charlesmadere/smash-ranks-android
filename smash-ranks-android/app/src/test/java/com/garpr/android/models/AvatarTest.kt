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
        private val AVATAR_1 = Avatar(null, null, null, null)
        private val AVATAR_2 = Avatar("large", "medium", "other", "small")
        private val AVATAR_3 = Avatar("large", "", "other", "small")
        private val AVATAR_4 = Avatar(" ", null, "", "small")
        private val AVATAR_5 = Avatar("", " ", "other", null)
        private val AVATAR_6 = Avatar()
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
