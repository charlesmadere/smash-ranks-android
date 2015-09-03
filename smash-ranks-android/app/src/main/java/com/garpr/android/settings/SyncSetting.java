package com.garpr.android.settings;


public final class SyncSetting extends Setting<SyncSetting> {


    public final BooleanSetting IsChargingNecessary;
    public final BooleanSetting IsEnabled;
    public final BooleanSetting IsScheduled;
    public final BooleanSetting IsWifiNecessary;
    public final LongSetting LastDate;




    SyncSetting(final String name, final String key) {
        super(name, key);

        IsChargingNecessary = new BooleanSetting(name, key + ".IS_CHARGING_NECESSARY");
        IsEnabled = new BooleanSetting(name, key + ".IS_ENABLED", true);
        IsScheduled = new BooleanSetting(name, key + ".IS_SCHEDULED");
        IsWifiNecessary = new BooleanSetting(name, key + ".IS_WIFI_NECESSARY");
        LastDate = new LongSetting(name, key + ".LAST_DATE");
    }


    @Override
    public void delete() {
        super.delete();
        IsChargingNecessary.delete();
        IsEnabled.delete();
        IsScheduled.delete();
        IsWifiNecessary.delete();
        LastDate.delete();
    }


    @Override
    public SyncSetting get() {
        return this;
    }


    @Override
    public void set(final SyncSetting newValue, final boolean notifyListeners) {
        IsChargingNecessary.set(newValue.IsChargingNecessary);
        IsEnabled.set(newValue.IsEnabled);
        IsScheduled.set(newValue.IsScheduled);
        IsWifiNecessary.set(newValue.IsWifiNecessary);
        LastDate.set(newValue.LastDate);
        super.set(newValue, notifyListeners);
    }


}
