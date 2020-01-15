package com.garpr.android.features.player

import com.garpr.android.BaseKoinTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import org.koin.test.inject

class SmashRosterAvatarUrlHelperTest : BaseKoinTest() {

    protected val smashRosterAvatarUrlHelper: SmashRosterAvatarUrlHelper by inject()

    companion object {
        private const val LARGE_AVATAR_PATH = "avatars/587a951dd2994e15c7dea9fe/large.jpg"
        private const val MEDIUM_AVATAR_PATH = "avatars/588999c5d2994e713ad63c6f/medium.jpg"
        private const val ORIGINAL_AVATAR_PATH = "avatars/588999c5d2994e713ad63b35/original.jpg"
        private const val SMALL_AVATAR_PATH = "avatars/5877eb55d2994e15c7dea98b/small.jpg"
    }

    @Test
    fun testGetAvatarUrlWithBlankString() {
        assertNull(smashRosterAvatarUrlHelper.getAvatarUrl(" "))
    }

    @Test
    fun testGetAvatarUrlWithEmptyString() {
        assertNull(smashRosterAvatarUrlHelper.getAvatarUrl(""))
    }

    @Test
    fun testGetAvatarUrlWithLargeAvatarPath() {
        val url = smashRosterAvatarUrlHelper.getAvatarUrl(LARGE_AVATAR_PATH)
        assertFalse(url.isNullOrBlank())
    }

    @Test
    fun testGetAvatarUrlWithMediumAvatarPath() {
        val url = smashRosterAvatarUrlHelper.getAvatarUrl(MEDIUM_AVATAR_PATH)
        assertFalse(url.isNullOrBlank())
    }

    @Test
    fun testGetAvatarUrlWithNull() {
        assertNull(smashRosterAvatarUrlHelper.getAvatarUrl(null))
    }

    @Test
    fun testGetAvatarUrlWithOriginalAvatarPath() {
        val url = smashRosterAvatarUrlHelper.getAvatarUrl(ORIGINAL_AVATAR_PATH)
        assertFalse(url.isNullOrBlank())
    }

    @Test
    fun testGetAvatarUrlWithSmallAvatarPath() {
        val url = smashRosterAvatarUrlHelper.getAvatarUrl(SMALL_AVATAR_PATH)
        assertFalse(url.isNullOrBlank())
    }

}
