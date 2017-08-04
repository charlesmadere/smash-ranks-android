package com.garpr.android.extensions

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.models.*

inline fun <reified T : Parcelable> createParcel(
        crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }

fun Parcel.readAbsPlayer(): AbsPlayer {
    return readOptionalAbsPlayer() ?: throw NullPointerException()
}

fun Parcel.readAbsPlayerList(): MutableList<AbsPlayer>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsPlayer>()

    for (i in 0..size - 1) {
        list.add(readAbsPlayer())
    }

    return list
}

fun Parcel.readOptionalAbsPlayer(): AbsPlayer? {
    val kind = readParcelable<AbsPlayer.Kind>(AbsPlayer.Kind::class.java.classLoader) ?: return null

    when (kind) {
        AbsPlayer.Kind.FAVORITE -> return readParcelable(FavoritePlayer::class.java.classLoader)
        AbsPlayer.Kind.FULL -> return readParcelable(FullPlayer::class.java.classLoader)
        AbsPlayer.Kind.LITE -> return readParcelable(LitePlayer::class.java.classLoader)
        else -> throw RuntimeException("illegal kind: " + kind)
    }
}

fun Parcel.writeAbsPlayer(player: AbsPlayer?, flags: Int) {
    if (player == null) {
        writeParcelable(null, flags)
    } else {
        writeParcelable(player.kind, flags)
        writeParcelable(player, flags)
    }
}

fun Parcel.writeAbsPlayerList(list: List<AbsPlayer>?, flags: Int) {
    val size = list?.size ?: 0
    writeInt(size)

    if (size == 0) {
        return
    }

    for (i in 0..size - 1) {
        writeAbsPlayer(list!![i], flags)
    }
}

fun Parcel.readAbsTournament(): AbsTournament {
    return readOptionalAbsTournament() ?: throw NullPointerException()
}

fun Parcel.readOptionalAbsTournament(): AbsTournament? {
    val kind = readParcelable<AbsTournament.Kind>(AbsTournament.Kind::class.java.classLoader) ?: return null

    when (kind) {
        AbsTournament.Kind.FULL -> return readParcelable(FullTournament::class.java.classLoader)
        AbsTournament.Kind.LITE -> return readParcelable(LiteTournament::class.java.classLoader)
        else -> throw RuntimeException("illegal kind: " + kind)
    }
}

fun Parcel.readAbsTournamentList(): MutableList<AbsTournament>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsTournament>()

    for (i in 0..size - 1) {
        list.add(readAbsTournament())
    }

    return list
}

fun Parcel.writeAbsTournament(tournament: AbsTournament?, flags: Int) {
    if (tournament == null) {
        writeParcelable(null, flags)
    } else {
        writeParcelable(tournament.kind, flags)
        writeParcelable(tournament, flags)
    }
}

fun Parcel.writeAbsTournamentList(list: List<AbsTournament>?, flags: Int) {
    val size = list?.size ?: 0
    writeInt(size)

    if (size == 0) {
        return
    }

    for (i in 0..size - 1) {
        writeAbsTournament(list!![i], flags)
    }
}

fun Parcel.readInteger(): Int? {
    val value = readString()

    if (value.isNullOrEmpty()) {
        return null
    } else {
        return value.toInt()
    }
}

fun Parcel.writeInteger(integer: Int?) {
    if (integer == null) {
        writeString(null)
    } else {
        writeString(integer.toString())
    }
}


fun Parcel.readRatingsMap(): Map<String, Rating>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val map = mutableMapOf<String, Rating>()

    for (i in 0..size - 1) {
        map.put(readString(), readParcelable<Parcelable>(Rating::class.java.classLoader) as Rating)
    }

    return map
}

fun Parcel.writeRatingsMap(map: Map<String, Rating>?, flags: Int) {
    val size = map?.size ?: 0
    writeInt(size)

    if (size == 0) {
        return
    }

    for (entry in map!!.entries) {
        writeString(entry.key)
        writeParcelable(entry.value, flags)
    }
}
