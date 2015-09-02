package com.garpr.android.misc;


public interface HeartbeatWithUi extends Heartbeat {


    void runOnUi(final Runnable action);


}
