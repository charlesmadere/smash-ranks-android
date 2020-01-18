package com.garpr.android.features.common.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.garpr.android.BaseAndroidTest
import org.junit.Rule

abstract class BaseViewModelTest : BaseAndroidTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

}
