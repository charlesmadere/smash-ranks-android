package com.garpr.android.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StringTest {

    companion object {
        // Misc websites
        private const val ARS_TECHNICA = "https://arstechnica.com/gaming/2019/12/ars-technicas-best-games-of-2019/"
        private const val GOOGLE = "https://www.google.com/"

        // Ringtone URIs taken from Android using the debugger
        private const val BEAT_BOX_ANDROID = "content://media/internal/audio/media/60"
        private const val DEFAULT_NOTIFICATION_SOUND = "content://settings/system/notification_sound"
        private const val END_NOTE = "content://media/external_primary/audio/media/28?title=End%20Note&canonical=1"
        private const val MAGIC = "content://media/external_primary/audio/media/27?title=Magic&canonical=1"
    }

    @Test
    fun testToURIWithArsTechnica() {
        val uri = ARS_TECHNICA.toJavaUri()
        assertEquals("https", uri?.scheme)
        assertEquals("arstechnica.com", uri?.authority)
        assertEquals("arstechnica.com", uri?.host)
        assertEquals("/gaming/2019/12/ars-technicas-best-games-of-2019/", uri?.rawPath)
        assertNull(uri?.rawQuery)
        assertEquals(ARS_TECHNICA, uri?.toString())
    }

    @Test
    fun testToURIWithBeatBoxAndroid() {
        val uri = BEAT_BOX_ANDROID.toJavaUri()
        assertEquals("content", uri?.scheme)
        assertEquals("media", uri?.authority)
        assertEquals("media", uri?.host)
        assertEquals("/internal/audio/media/60", uri?.rawPath)
        assertNull(uri?.rawQuery)
        assertEquals(BEAT_BOX_ANDROID, uri?.toString())
    }

    @Test
    fun testToURIWithBlank() {
        assertNull(" ".toJavaUri())
    }

    @Test
    fun testToURIWithDefaultNotificationSound() {
        val uri = DEFAULT_NOTIFICATION_SOUND.toJavaUri()
        assertEquals("content", uri?.scheme)
        assertEquals("settings", uri?.authority)
        assertEquals("settings", uri?.host)
        assertEquals("/system/notification_sound", uri?.rawPath)
        assertNull(uri?.rawQuery)
        assertEquals(DEFAULT_NOTIFICATION_SOUND, uri?.toString())
    }

    @Test
    fun testToURIWithEmpty() {
        assertNull("".toJavaUri())
    }

    @Test
    fun testToURIWithEndNote() {
        val uri = END_NOTE.toJavaUri()
        assertEquals("content", uri?.scheme)
        assertEquals("media", uri?.authority)
        assertEquals("media", uri?.host)
        assertEquals("/external_primary/audio/media/28", uri?.rawPath)
        assertEquals("title=End%20Note&canonical=1", uri?.rawQuery)
        assertEquals(END_NOTE, uri?.toString())
    }

    @Test
    fun testToURIWithGoogle() {
        val uri = GOOGLE.toJavaUri()
        assertEquals("https", uri?.scheme)
        assertEquals("www.google.com", uri?.authority)
        assertEquals("www.google.com", uri?.host)
        assertEquals("/", uri?.rawPath)
        assertNull(uri?.rawQuery)
        assertEquals(GOOGLE, uri?.toString())
    }

    @Test
    fun testToURIWithMagic() {
        val uri = MAGIC.toJavaUri()
        assertEquals("content", uri?.scheme)
        assertEquals("media", uri?.authority)
        assertEquals("media", uri?.host)
        assertEquals("/external_primary/audio/media/27", uri?.rawPath)
        assertEquals("title=Magic&canonical=1", uri?.rawQuery)
        assertEquals(MAGIC, uri?.toString())
    }

    @Test
    fun testToURIWithNull() {
        assertNull((null as String?).toJavaUri())
    }

}
