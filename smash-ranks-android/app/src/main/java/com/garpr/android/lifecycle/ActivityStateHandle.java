package com.garpr.android.lifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface ActivityStateHandle {

    @Nullable
    ActivityState getActivityState();

    @Nullable
    Bundle getSavedInstanceState();

}
