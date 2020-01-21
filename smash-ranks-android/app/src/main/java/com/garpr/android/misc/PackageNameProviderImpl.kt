package com.garpr.android.misc

import android.content.Context

class PackageNameProviderImpl(
        context: Context
) : PackageNameProvider {

    override val packageName: String = context.packageName

}
