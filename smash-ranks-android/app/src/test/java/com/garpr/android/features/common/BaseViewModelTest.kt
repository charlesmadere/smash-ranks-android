package com.garpr.android.features.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garpr.android.BaseAndroidKoinTest
import org.junit.Rule

abstract class BaseViewModelTest : BaseAndroidKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

}
