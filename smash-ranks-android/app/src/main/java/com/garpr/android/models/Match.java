package com.garpr.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.garpr.android.misc.ParcelableUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Comparator;

public class Match implements Parcelable {

    @SerializedName("opponent")
    private AbsPlayer mOpponent;

    @SerializedName("tournament")
    private AbsTournament mTournament;

    @SerializedName("result")
    private Result mResult;


    public AbsPlayer getOpponent() {
        return mOpponent;
    }

    public String getOpponentId() {
        return mOpponent.getId();
    }

    public String getOpponentName() {
        return mOpponent.getName();
    }

    public Result getResult() {
        return mResult;
    }

    public AbsTournament getTournament() {
        return mTournament;
    }

    public SimpleDate getTournamentDate() {
        return mTournament.getDate();
    }

    public String getTournamentId() {
        return mTournament.getId();
    }

    public String getTournamentName() {
        return mTournament.getName();
    }

    @Override
    public String toString() {
        return getOpponentName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        ParcelableUtils.writeAbsPlayer(mOpponent, dest, flags);
        ParcelableUtils.writeAbsTournament(mTournament, dest, flags);
        dest.writeParcelable(mResult, flags);
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(final Parcel source) {
            final Match m = new Match();
            m.mOpponent = ParcelableUtils.readAbsPlayer(source);
            m.mTournament = ParcelableUtils.readAbsTournament(source);
            m.mResult = source.readParcelable(Result.class.getClassLoader());
            return m;
        }

        @Override
        public Match[] newArray(final int size) {
            return new Match[size];
        }
    };

    public static final Comparator<Match> CHRONOLOGICAL_ORDER = new Comparator<Match>() {
        @Override
        public int compare(final Match o1, final Match o2) {
            return SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.getTournamentDate(),
                    o2.getTournamentDate());
        }
    };

    public static final Comparator<Match> REVERSE_CHRONOLOGICAL_ORDER = new Comparator<Match>() {
        @Override
        public int compare(final Match o1, final Match o2) {
            return CHRONOLOGICAL_ORDER.compare(o2, o1);
        }
    };

    public static final JsonDeserializer<Match> JSON_DESERIALIZER = new JsonDeserializer<Match>() {
        @Override
        public Match deserialize(final JsonElement json, final Type typeOfT,
                final JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.isJsonNull()) {
                return null;
            }

            final JsonObject jsonObject = json.getAsJsonObject();

            final Match match = new Match();
            match.mOpponent = new LitePlayer(jsonObject.get("opponent_id").getAsString(),
                    jsonObject.get("opponent_name").getAsString());
            match.mTournament = new LiteTournament(
                    jsonObject.get("tournament_id").getAsString(),
                    jsonObject.get("tournament_name").getAsString(),
                    (SimpleDate) context.deserialize(jsonObject.get("tournament_date"), SimpleDate.class));

            return match;
        }
    };


    public enum Result implements Parcelable {
        @SerializedName("excluded")
        EXCLUDED,

        @SerializedName("lose")
        LOSE,

        @SerializedName("win")
        WIN;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(final Parcel source) {
                return values()[source.readInt()];
            }

            @Override
            public Result[] newArray(final int size) {
                return new Result[size];
            }
        };
    }

}
