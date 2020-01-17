package com.garpr.android.features.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garpr.android.BaseKoinTest
import org.junit.Rule

abstract class BaseViewModelTest : BaseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

}
