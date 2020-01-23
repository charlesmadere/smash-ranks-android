package com.garpr.android.misc

import com.garpr.android.test.BaseAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Test
import org.koin.core.inject

class PackageNameProviderTest : BaseAndroidTest() {

    protected val packageNameProvider: PackageNameProvider by inject()

    @Test
    fun testPackageName() {
        assertFalse(packageNameProvider.packageName.isBlank())
    }

}
