package com.garpr.android.settings;


public final class SyncSetting extends Setting<SyncSetting> {


    public final BooleanSetting IsAllowed;
    public final BooleanSetting IsChargingNecessary;
    public final BooleanSetting IsScheduled;
    public final BooleanSetting IsWifiNecessary;
    public final LongSetting LastDate;




    public SyncSetting(final String name, final String key) {
        super(name, key);

        IsAllowed = new BooleanSetting(name, key + ".IS_ALLOWED", true);
        IsChargingNecessary = new BooleanSetting(name, key + ".IS_CHARGING_NECESSARY");
        IsScheduled = new BooleanSetting(name, key + ".IS_SCHEDULED");
        IsWifiNecessary = new BooleanSetting(name, key + ".IS_WIFI_NECESSARY", true);
        LastDate = new LongSetting(name, key + ".LAST_DATE");
    }


    @Override
    public void delete(final boolean notifyListeners) {
        IsAllowed.delete();
        IsChargingNecessary.delete();
        IsScheduled.delete();
        IsWifiNecessary.delete();
        LastDate.delete();
        super.delete(notifyListeners);
    }


    @Override
    public boolean exists() {
        return super.exists() || IsAllowed.exists() || IsChargingNecessary.exists() ||
                IsScheduled.exists() || IsWifiNecessary.exists() || LastDate.exists();
    }


    @Override
    public SyncSetting get() {
        return this;
    }


    @Override
    public void set(final SyncSetting newValue, final boolean notifyListeners) {
        IsAllowed.set(newValue.IsAllowed);
        IsChargingNecessary.set(newValue.IsChargingNecessary);
        IsScheduled.set(newValue.IsScheduled);
        IsWifiNecessary.set(newValue.IsWifiNecessary);
        LastDate.set(newValue.LastDate);
        super.set(newValue, notifyListeners);
    }


}
