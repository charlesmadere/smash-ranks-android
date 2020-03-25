package com.garpr.android.features.common.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garpr.android.test.BaseAndroidTest
import org.junit.Rule

abstract class BaseViewModelTest : BaseAndroidTest() {

    /**
     * This fixes issues that occur when using LiveData in a test environment. From the Android
     * documentation for this class:
     *
     * > A JUnit Test Rule that swaps the background executor used by the Architecture Components
     * > with a different one which executes each task synchronously.
     *
     * https://developer.android.com/reference/androidx/arch/core/executor/testing/InstantTaskExecutorRule
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

}
