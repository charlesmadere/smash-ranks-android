package com.garpr.android.extensions

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.AbsRegion
import com.garpr.android.data.models.AbsTournament
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.FullPlayer
import com.garpr.android.data.models.FullTournament
import com.garpr.android.data.models.LitePlayer
import com.garpr.android.data.models.LiteRegion
import com.garpr.android.data.models.LiteTournament
import com.garpr.android.data.models.RankedPlayer
import com.garpr.android.data.models.Rating
import com.garpr.android.data.models.Region

inline fun <reified T : Parcelable> createParcel(
        crossinline createFromParcel: (Parcel) -> T?) = object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }

fun Parcel.readAbsPlayer(): AbsPlayer {
    return requireNotNull(readOptionalAbsPlayer())
}

fun Parcel.readOptionalAbsPlayer(): AbsPlayer? {
    @Suppress("MoveVariableDeclarationIntoWhen")
    val kind = readParcelable<AbsPlayer.Kind>(AbsPlayer.Kind::class.java.classLoader) ?: return null

    return when (kind) {
        AbsPlayer.Kind.FAVORITE -> readParcelable(FavoritePlayer::class.java.classLoader)
        AbsPlayer.Kind.FULL -> readParcelable(FullPlayer::class.java.classLoader)
        AbsPlayer.Kind.LITE -> readParcelable(LitePlayer::class.java.classLoader)
        AbsPlayer.Kind.RANKED -> readParcelable(RankedPlayer::class.java.classLoader)
    }
}

fun Parcel.readAbsPlayerList(): MutableList<AbsPlayer>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsPlayer>()

    repeat(size) {
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

    repeat(size) { index ->
        writeAbsPlayer(list.require(index), flags)
    }
}

fun Parcel.readAbsRegion(): AbsRegion {
    return requireNotNull(readOptionalAbsRegion())
}

fun Parcel.readOptionalAbsRegion(): AbsRegion? {
    @Suppress("MoveVariableDeclarationIntoWhen")
    val kind = readParcelable<AbsRegion.Kind>(AbsRegion::class.java.classLoader) ?: return null

    return when (kind) {
        AbsRegion.Kind.LITE -> readParcelable(LiteRegion::class.java.classLoader)
        AbsRegion.Kind.FULL -> readParcelable(Region::class.java.classLoader)
    }
}

fun Parcel.readAbsRegionList(): List<AbsRegion>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsRegion>()

    repeat(size) {
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

    repeat(size) { index ->
        writeAbsRegion(list.require(index), flags)
    }
}

fun Parcel.readAbsTournament(): AbsTournament {
    return requireNotNull(readOptionalAbsTournament())
}

fun Parcel.readOptionalAbsTournament(): AbsTournament? {
    @Suppress("MoveVariableDeclarationIntoWhen")
    val kind = readParcelable<AbsTournament.Kind>(AbsTournament.Kind::class.java.classLoader) ?: return null

    return when (kind) {
        AbsTournament.Kind.FULL -> readParcelable(FullTournament::class.java.classLoader)
        AbsTournament.Kind.LITE -> readParcelable(LiteTournament::class.java.classLoader)
    }
}

fun Parcel.readAbsTournamentList(): MutableList<AbsTournament>? {
    val size = readInt()

    if (size == 0) {
        return null
    }

    val list = mutableListOf<AbsTournament>()

    repeat(size) {
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

    repeat(size) { index ->
        writeAbsTournament(list.require(index), flags)
    }
}

fun Parcel.requireBoolean(): Boolean {
    return requireNotNull(readBoolean())
}

fun Parcel.readBoolean(): Boolean? {
    return readValue(Boolean::class.java.classLoader) as Boolean?
}

fun Parcel.writeBoolean(boolean: Boolean?) {
    writeValue(boolean)
}

fun Parcel.readInteger(): Int? {
    return readValue(Integer::class.java.classLoader) as Int?
}

fun Parcel.writeInteger(integer: Int?) {
    writeValue(integer)
}

fun <T : Parcelable> Parcel.requireParcelable(loader: ClassLoader?): T {
    checkNotNull(loader) { "loader is null" }
    return requireNotNull(readParcelable(loader))
}

fun Parcel.readRatingsMap(): Map<String, Rating>? {
    val bundle = readBundle(Rating::class.java.classLoader) ?: return null
    val map = mutableMapOf<String, Rating>()

    bundle.keySet().forEach { key ->
        map[key] = bundle.requireParcelable(key)
    }

    return map
}

fun Parcel.writeRatingsMap(map: Map<String, Rating>?) {
    if (map.isNullOrEmpty()) {
        writeBundle(null)
        return
    }

    val bundle = Bundle(map.size)

    for ((key, value) in map) {
        bundle.putParcelable(key, value)
    }

    writeBundle(bundle)
}

fun Parcel.requireString(): String {
    return requireNotNull(readString())
}

fun Parcel.readStringMap(): Map<String, String>? {
    val bundle = readBundle(String::class.java.classLoader) ?: return null
    val map = mutableMapOf<String, String>()

    bundle.keySet().forEach { key ->
        map[key] = bundle.requireString(key)
    }

    return map
}

fun Parcel.writeStringMap(map: Map<String, String>?) {
    if (map.isNullOrEmpty()) {
        writeBundle(null)
        return
    }

    val bundle = Bundle(map.size)

    for ((key, value) in map) {
        bundle.putString(key, value)
    }

    writeBundle(bundle)
}
