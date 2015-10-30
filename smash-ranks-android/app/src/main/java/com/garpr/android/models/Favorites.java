package com.garpr.android.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.misc.Constants;
import com.garpr.android.settings.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


public class Favorites implements Parcelable {


    private static final int VERSION = 1;

    private final HashMap<Region, ArrayList<Player>> mMap;




    public Favorites() {
        mMap = new HashMap<>();
    }


    public Favorites(final JSONObject json) throws JSONException {
        final int version = json.getInt(Constants.VERSION);

        switch (version) {
            case 1:
                mMap = readInVersion1(json);
                break;

            default:
                throw new RuntimeException("unknown handling for version " + version);
        }
    }


    private Favorites(final Parcel source) {
        final int mapSize = source.readInt();
        mMap = new HashMap<>(mapSize);

        for (int i = 0; i < mapSize; ++i) {
            final Region region = source.readParcelable(Region.class.getClassLoader());
            final ArrayList<Player> players = source.createTypedArrayList(Player.CREATOR);
            mMap.put(region, players);
        }
    }


    public void add(final Player player) {
        final Region region = Settings.Region.get();
        ArrayList<Player> list = mMap.get(region);

        if (list == null) {
            list = new ArrayList<>(1);
        }

        list.add(player);
        Collections.sort(list, Player.ALPHABETICAL_ORDER);
        mMap.put(region, list);
    }


    public boolean contains(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player can't be null");
        }

        if (isEmpty()) {
            return false;
        }

        for (final Entry<Region, ArrayList<Player>> entry : mMap.entrySet()) {
            final ArrayList<Player> players = entry.getValue();

            if (players.contains(player)) {
                return true;
            }
        }

        return false;
    }


    public ArrayList<ListItem> flatten() {
        final ArrayList<ListItem> listItems = new ArrayList<>();

        final ArrayList<Region> regions = new ArrayList<>(mMap.keySet());
        Collections.sort(regions, Region.ALPHABETICAL_ORDER);

        for (final Region region : regions) {
            listItems.add(ListItem.createRegion(region));
            final ArrayList<Player> players = mMap.get(region);

            if (players != null && !players.isEmpty()) {
                Collections.sort(players, Player.ALPHABETICAL_ORDER);

                for (final Player player : players) {
                    listItems.add(ListItem.createPlayer(player));
                }
            }
        }

        listItems.trimToSize();
        return listItems;
    }


    public Region findRegionForPlayer(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player can't be null");
        }

        if (isEmpty()) {
            return null;
        }

        for (final Entry<Region, ArrayList<Player>> entry : mMap.entrySet()) {
            final ArrayList<Player> players = entry.getValue();

            if (players.contains(player)) {
                return entry.getKey();
            }
        }

        return null;
    }


    public HashMap<Region, ArrayList<Player>> get() {
        return mMap;
    }


    public boolean isEmpty() {
        return mMap.isEmpty();
    }


    private HashMap<Region, ArrayList<Player>> readInVersion1(final JSONObject json)
            throws JSONException {
        final JSONArray mapJSON = json.getJSONArray(Constants.MAP);
        final int mapSize = mapJSON.length();
        final HashMap<Region, ArrayList<Player>> map = new HashMap<>(mapSize);

        for (int i = 0; i < mapSize; ++i) {
            final JSONObject favorite = mapJSON.getJSONObject(i);

            final JSONObject regionJSON = favorite.getJSONObject(Constants.REGION);
            final Region region = new Region(regionJSON);

            final JSONArray playersJSON = favorite.getJSONArray(Constants.PLAYERS);
            final int playersJSONSize = playersJSON.length();
            final ArrayList<Player> players = new ArrayList<>(playersJSONSize);

            for (int j = 0; j < playersJSONSize; ++j) {
                final JSONObject playerJSON = playersJSON.getJSONObject(j);
                players.add(new Player(playerJSON));
            }

            map.put(region, players);
        }

        return map;
    }


    public void remove(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player can't be null");
        }

        if (isEmpty()) {
            return;
        }

        final Region region = findRegionForPlayer(player);
        final ArrayList<Player> players = mMap.get(region);
        players.remove(player);

        if (players.isEmpty()) {
            mMap.remove(region);
        }
    }


    public JSONObject toJSON() {
        try {
            final JSONObject json = new JSONObject();
            json.put(Constants.VERSION, VERSION);

            final JSONArray map = new JSONArray();

            for (final Entry<Region, ArrayList<Player>> entry : mMap.entrySet()) {
                final JSONObject favorite = new JSONObject();

                final Region region = entry.getKey();
                favorite.put(Constants.REGION, region.toJSON());

                final ArrayList<Player> players = entry.getValue();
                final JSONArray playersJSON = new JSONArray();

                for (final Player player : players) {
                    playersJSON.put(player.toJSON());
                }

                favorite.put(Constants.PLAYERS, playersJSON);
                map.put(favorite);
            }

            json.put(Constants.MAP, map);

            return json;
        } catch (final JSONException e) {
            // this should never happen
            throw new RuntimeException(e);
        }
    }




    /*
     * Code necessary for the Android Parcelable interface is below. Read more here:
     * https://developer.android.com/intl/es/reference/android/os/Parcelable.html
     */


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(mMap.size());

        for (final Entry<Region, ArrayList<Player>> entry : mMap.entrySet()) {
            dest.writeParcelable(entry.getKey(), flags);
            dest.writeTypedList(entry.getValue());
        }
    }


    public static final Creator<Favorites> CREATOR = new Creator<Favorites>() {
        @Override
        public Favorites createFromParcel(final Parcel source) {
            return new Favorites(source);
        }


        @Override
        public Favorites[] newArray(final int size) {
            return new Favorites[size];
        }
    };




    public static class ListItem {


        private Player mPlayer;
        private Region mRegion;
        private Type mType;


        private static ListItem createPlayer(final Player player) {
            final ListItem item = new ListItem();
            item.mPlayer = player;
            item.mType = Type.PLAYER;

            return item;
        }


        private static ListItem createRegion(final Region region) {
            final ListItem item = new ListItem();
            item.mRegion = region;
            item.mType = Type.REGION;

            return item;
        }


        public Player getPlayer() {
            return mPlayer;
        }


        public Region getRegion() {
            return mRegion;
        }


        public Type getType() {
            return mType;
        }


        public enum Type {
            PLAYER, REGION
        }


    }


}
