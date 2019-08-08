package com.garpr.android.extensions

import com.garpr.android.BaseTest
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BehaviorSubjectTest : BaseTest() {

    companion object {
        private const val HELLO_WORLD = "Hello, World!"
    }

    @Test
    fun testRequireValueWithDefault() {
        val subject = BehaviorSubject.createDefault(HELLO_WORLD)
        assertEquals(subject.value, subject.requireValue())
    }

    @Test
    fun testRequireValueWithoutDefault() {
        val subject = BehaviorSubject.create<String>()
        var value: String? = null
        var throwable: Throwable? = null

        try {
            value = subject.requireValue()
        } catch (t: Throwable) {
            throwable = t
        }

        assertNull(value)
        assertNotNull(throwable)
    }

}