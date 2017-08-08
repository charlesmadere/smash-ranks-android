package com.garpr.android.extensions

import android.os.Bundle
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

fun Parcel.readOptionalAbsPlayer(): AbsPlayer? {
    val kind = readParcelable<AbsPlayer.Kind>(AbsPlayer.Kind::class.java.classLoader) ?: return null

    when (kind) {
        AbsPlayer.Kind.FAVORITE -> return readParcelable(FavoritePlayer::class.java.classLoader)
        AbsPlayer.Kind.FULL -> return readParcelable(FullPlayer::class.java.classLoader)
        AbsPlayer.Kind.LITE -> return readParcelable(LitePlayer::class.java.classLoader)
        else -> throw RuntimeException("illegal kind: " + kind)
    }
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

fun Parcel.readAbsRegion(): AbsRegion {
    return readOptionalAbsRegion() ?: throw NullPointerException()
}

fun Parcel.readOptionalAbsRegion(): AbsRegion? {
    val kind = readParcelable<AbsRegion.Kind>(AbsRegion::class.java.classLoader) ?: return null

    when (kind) {
        AbsRegion.Kind.LITE -> return readParcelable(LiteRegion::class.java.classLoader)
        AbsRegion.Kind.FULL -> return readParcelable(Region::class.java.classLoader)
        else -> throw RuntimeException("illegal kind: " + kind)
    }
}

fun Parcel.readAbsRegionList(): List<AbsRegion>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsRegion>()

    for (i in 0..size - 1) {
        list.add(readAbsRegion())
    }

    return list
}

fun Parcel.writeAbsRegion(region: AbsRegion?, flags: Int) {
    if (region == null) {
        writeParcelable(null, flags)
    } else {
        writeParcelable(region.kind, flags)
        writeParcelable(region, flags)
    }
}

fun Parcel.writeAbsRegionList(list: List<AbsRegion>?, flags: Int) {
    val size = list?.size ?: 0
    writeInt(size)

    if (size == 0) {
        return
    }

    for (i in 0..size - 1) {
        writeAbsRegion(list!![i], flags)
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
    return readValue(Integer::class.java.classLoader) as Int?
}

fun Parcel.writeInteger(integer: Int?) {
    writeValue(integer)
}

fun Parcel.readRatingsMap(): Map<String, Rating>? {
    val bundle = readBundle(Rating::class.java.classLoader) ?: return null
    val map = mutableMapOf<String, Rating>()

    for (key in bundle.keySet()) {
        map.put(key, bundle.getParcelable(key))
    }

    return map
}

fun Parcel.writeRatingsMap(map: Map<String, Rating>?) {
    val size = map?.size ?: 0

    if (size == 0) {
        writeBundle(null)
        return
    }

    val bundle = Bundle(size)

    for ((key, value) in map!!) {
        bundle.putParcelable(key, value)
    }

    writeBundle(bundle)
}
