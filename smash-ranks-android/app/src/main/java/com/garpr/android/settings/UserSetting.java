package com.garpr.android.settings;


public final class UserSetting extends Setting<UserSetting> {


    public final PlayerListSetting Favorites;
    public final PlayerSetting Player;
    public final RegionSetting Region;




    public UserSetting(final String name, final String key) {
        super(name, key);

        Favorites = new PlayerListSetting(name, key + ".FAVORITES");
        Player = new PlayerSetting(name, key + ".PLAYER");
        Region = new RegionSetting(name, key + ".REGION");
    }


    @Override
    public void delete(final boolean notifyListeners) {
        Favorites.delete();
        Player.delete();
        Region.delete();
        super.delete(notifyListeners);
    }


    @Override
    public boolean exists() {
        return super.exists() || Favorites.exists() || Player.exists() || Region.exists();
    }


    @Override
    public UserSetting get() {
        return this;
    }


    @Override
    public void set(final UserSetting newValue, final boolean notifyListeners) {
        Favorites.set(newValue.Favorites, notifyListeners);
        Player.set(newValue.Player, notifyListeners);
        Region.set(newValue.Region, notifyListeners);
        super.set(newValue, notifyListeners);
    }


}
