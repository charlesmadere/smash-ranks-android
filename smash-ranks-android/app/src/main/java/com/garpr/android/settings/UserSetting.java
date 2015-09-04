package com.garpr.android.settings;


public final class UserSetting extends Setting<UserSetting> {


    public final PlayerSetting Player;
    public final RegionSetting Region;




    public UserSetting(final String name, final String key) {
        super(name, key);

        Player = new PlayerSetting(name, key + ".PLAYER");
        Region = new RegionSetting(name, key + ".REGION");
    }


    @Override
    public void delete() {
        Player.delete();
        Region.delete();
        super.delete();
    }


    @Override
    public boolean exists() {
        return super.exists() || Player.exists() || Region.exists();
    }


    @Override
    public UserSetting get() {
        return this;
    }


    @Override
    public void set(final UserSetting newValue, final boolean notifyListeners) {
        Player.set(newValue.Player);
        Region.set(newValue.Region);
        super.set(newValue, notifyListeners);
    }


}
