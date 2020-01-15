package com.garpr.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class BaseViewModelTest : BaseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

}
