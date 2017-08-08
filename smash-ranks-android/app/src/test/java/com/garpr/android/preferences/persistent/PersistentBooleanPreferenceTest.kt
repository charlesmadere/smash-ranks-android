package com.garpr.android.preferences.persistent

import com.garpr.android.BaseTest
import com.garpr.android.BuildConfig
import com.garpr.android.preferences.KeyValueStore
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class PersistentBooleanPreferenceTest : BaseTest() {

    @Inject
    lateinit protected var keyValueStore: KeyValueStore


    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testAppComponent.inject(this)
    }

    @Test
    @Throws(Exception::class)
    fun testNonNullGetDefaultValue() {
        val preference = PersistentBooleanPreference("boolean",false, keyValueStore)
        assertEquals(preference.defaultValue, java.lang.Boolean.FALSE)

        preference.set(true)
        assertTrue(preference.defaultValue == false)
    }

    @Test
    @Throws(Exception::class)
    fun testNullGetDefaultValue() {
        val preference = PersistentBooleanPreference("boolean", null, keyValueStore)
        assertNull(preference.defaultValue)

        preference.set(true)
        assertNull(preference.defaultValue)
    }

}
