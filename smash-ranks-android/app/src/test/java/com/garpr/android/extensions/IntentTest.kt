package com.garpr.android.extensions

import android.content.Intent
import com.garpr.android.BaseTest
import com.garpr.android.data.models.LitePlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class IntentTest : BaseTest() {

    companion object {
        private const val EXTRA_BLAH = "blah"
        private const val STRING = "Hello, World!"

        private val PLAYER = LitePlayer(
                id = "587a951dd2994e15c7dea9fe",
                name = "Charlezard"
        )
    }

    @Test
    fun testPutOptionalExtra() {
        val intent = Intent().putOptionalExtra(EXTRA_BLAH, PLAYER)
        assertTrue(intent.hasExtra(EXTRA_BLAH))
        assertEquals(PLAYER, intent.getParcelableExtra(EXTRA_BLAH))
    }

    @Test
    fun testPutOptionalExtraWithNull() {
        val intent = Intent().putOptionalExtra(EXTRA_BLAH, null)
        assertFalse(intent.hasExtra(EXTRA_BLAH))
        assertNull(intent.getParcelableExtra(EXTRA_BLAH))
    }

    @Test
    fun testRequireStringExtraWithString() {
        val intent = Intent().putExtra(EXTRA_BLAH, STRING)
        assertTrue(intent.hasExtra(EXTRA_BLAH))
        assertEquals(STRING, intent.requireStringExtra(EXTRA_BLAH))
    }

    @Test
    fun testRequireStringExtraWithoutString() {
        val intent = Intent()
        var string: String? = null
        var throwable: Throwable? = null

        try {
            string = intent.requireStringExtra(EXTRA_BLAH)
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(string)
        assertNotNull(throwable)
    }

}
