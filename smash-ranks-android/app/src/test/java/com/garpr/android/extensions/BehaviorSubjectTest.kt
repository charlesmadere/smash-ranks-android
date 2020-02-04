package com.garpr.android.extensions

import com.garpr.android.test.BaseTest
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class BehaviorSubjectTest : BaseTest() {

    @Test
    fun testRequireValueWithDefault() {
        val subject = BehaviorSubject.createDefault(HELLO_WORLD)
        assertEquals(HELLO_WORLD, subject.requireValue())

        subject.onNext(BLAH)
        assertEquals(BLAH, subject.requireValue())
    }

    @Test
    fun testRequireValueWithoutDefault() {
        val subject = BehaviorSubject.create<String>()
        var value: String? = null

        assertThrows(Throwable::class.java) {
            value = subject.requireValue()
        }

        assertNull(value)

        subject.onNext(SHEIK)
        assertEquals(SHEIK, subject.requireValue())
    }

    companion object {
        private const val BLAH = "Blah"
        private const val HELLO_WORLD = "Hello, World!"
        private const val SHEIK = "Sheik"
    }

}
