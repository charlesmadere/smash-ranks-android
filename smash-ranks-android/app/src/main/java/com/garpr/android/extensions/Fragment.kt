package com.garpr.android.extensions

import androidx.fragment.app.Fragment
import com.garpr.android.dagger.AppComponent

val Fragment.appComponent: AppComponent
    get() = requireContext().appComponent
