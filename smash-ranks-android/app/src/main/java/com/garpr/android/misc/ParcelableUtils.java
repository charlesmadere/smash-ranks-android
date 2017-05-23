package com.garpr.android.misc;

import android.os.Parcel;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.garpr.android.models.AbsPlayer;
import com.garpr.android.models.AbsTournament;
import com.garpr.android.models.FavoritePlayer;
import com.garpr.android.models.FullPlayer;
import com.garpr.android.models.FullTournament;
import com.garpr.android.models.LitePlayer;
import com.garpr.android.models.LiteTournament;

import java.util.ArrayList;
import java.util.List;

public final class ParcelableUtils {

    @Nullable
    public static AbsPlayer readAbsPlayer(final Parcel source) {
        final AbsPlayer.Kind kind = source.readParcelable(AbsPlayer.Kind.class.getClassLoader());

        if (kind == null) {
            return null;
        }

        switch (kind) {
            case FAVORITE:
                return source.readParcelable(FavoritePlayer.class.getClassLoader());

            case FULL:
                return source.readParcelable(FullPlayer.class.getClassLoader());

            case LITE:
                return source.readParcelable(LitePlayer.class.getClassLoader());

            default:
                throw new RuntimeException("illegal kind: " + kind);
        }
    }

    @Nullable
    public static ArrayList<AbsPlayer> readAbsPlayerList(final Parcel source) {
        final int size = source.readInt();

        if (size == 0) {
            return null;
        }

        final ArrayList<AbsPlayer> list = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            list.add(readAbsPlayer(source));
        }

        return list;
    }

    public static void writeAbsPlayer(@Nullable final AbsPlayer player, final Parcel dest,
            final int flags) {
        if (player == null) {
            dest.writeParcelable(null, flags);
        } else {
            dest.writeParcelable(player.getKind(), flags);
            dest.writeParcelable(player, flags);
        }
    }

    public static void writeAbsPlayerList(@Nullable final List<AbsPlayer> list, final Parcel dest,
            final int flags) {
        final int size = list == null ? 0 : list.size();
        dest.writeInt(size);

        if (size == 0) {
            return;
        }

        for (int i = 0; i < size; ++i) {
            writeAbsPlayer(list.get(i), dest, flags);
        }
    }

    @Nullable
    public static AbsTournament readAbsTournament(final Parcel source) {
        final AbsTournament.Kind kind = source.readParcelable(AbsTournament.Kind.class.getClassLoader());

        if (kind == null) {
            return null;
        }

        switch (kind) {
            case FULL:
                return source.readParcelable(FullTournament.class.getClassLoader());

            case LITE:
                return source.readParcelable(LiteTournament.class.getClassLoader());

            default:
                throw new RuntimeException("illegal kind: " + kind);
        }
    }

    @Nullable
    public static ArrayList<AbsTournament> readAbsTournamentList(final Parcel source) {
        final int size = source.readInt();

        if (size == 0) {
            return null;
        }

        final ArrayList<AbsTournament> list = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            list.add(readAbsTournament(source));
        }

        return list;
    }

    public static void writeAbsTournament(@Nullable final AbsTournament tournament,
            final Parcel dest, final int flags) {
        if (tournament == null) {
            dest.writeParcelable(null, flags);
        } else {
            dest.writeParcelable(tournament.getKind(), flags);
            dest.writeParcelable(tournament, flags);
        }
    }

    public static void writeAbsTournamentList(@Nullable final List<AbsTournament> list,
            final Parcel dest, final int flags) {
        final int size = list == null ? 0 : list.size();
        dest.writeInt(size);

        if (size == 0) {
            return;
        }

        for (int i = 0; i < size; ++i) {
            writeAbsTournament(list.get(i), dest, flags);
        }
    }

    @Nullable
    public static Integer readInteger(final Parcel source) {
        final String value = source.readString();

        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            return Integer.valueOf(value);
        }
    }

    public static void writeInteger(@Nullable final Integer integer, final Parcel dest) {
        if (integer == null) {
            dest.writeString(null);
        } else {
            dest.writeString(integer.toString());
        }
    }

}
