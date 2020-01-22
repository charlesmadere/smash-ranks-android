package com.garpr.android.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.garpr.android.AndroidTestApp

class AndroidTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?,
            context: Context?): Application {
        return super.newApplication(cl, AndroidTestApp::class.java.name, context)
    }

}
