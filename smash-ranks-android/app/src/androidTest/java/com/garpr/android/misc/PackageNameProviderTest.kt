package com.garpr.android.misc

import com.garpr.android.BaseAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Test
import org.koin.test.inject

class PackageNameProviderTest : BaseAndroidTest() {

    protected val packageNameProvider: PackageNameProvider by inject()

    @Test
    fun testPackageName() {
        assertFalse(packageNameProvider.packageName.isBlank())
    }

}
