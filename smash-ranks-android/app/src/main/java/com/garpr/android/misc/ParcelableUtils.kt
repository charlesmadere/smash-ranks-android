package com.garpr.android.misc

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.models.AbsTournament
import com.garpr.android.models.FullTournament
import com.garpr.android.models.LiteTournament
import com.garpr.android.models.Rating

object ParcelableUtils {

    fun readAbsTournament(source: Parcel): AbsTournament {
        val kind = source.readParcelable<AbsTournament.Kind>(AbsTournament.Kind::class.java.classLoader)

        when (kind) {
            AbsTournament.Kind.FULL -> return source.readParcelable<AbsTournament>(FullTournament::class.java.classLoader)
            AbsTournament.Kind.LITE -> return source.readParcelable<AbsTournament>(LiteTournament::class.java.classLoader)
            else -> throw RuntimeException("illegal kind: " + kind)
        }
    }

    fun readAbsTournamentList(source: Parcel): MutableList<AbsTournament>? {
        val size = source.readInt()

        if (size == 0) {
            return null
        }

        val list = mutableListOf<AbsTournament>()

        for (i in 0..size - 1) {
            list.add(readAbsTournament(source))
        }

        return list
    }

    fun writeAbsTournament(tournament: AbsTournament?, dest: Parcel, flags: Int) {
        if (tournament == null) {
            dest.writeParcelable(null, flags)
        } else {
            dest.writeParcelable(tournament.kind, flags)
            dest.writeParcelable(tournament, flags)
        }
    }

    fun writeAbsTournamentList(list: List<AbsTournament>?, dest: Parcel, flags: Int) {
        val size = list?.size ?: 0
        dest.writeInt(size)

        if (size == 0) {
            return
        }

        for (i in 0..size - 1) {
            writeAbsTournament(list!![i], dest, flags)
        }
    }

    fun readRatingsMap(source: Parcel): Map<String, Rating>? {
        val size = source.readInt()

        if (size == 0) {
            return null
        }

        val map = mutableMapOf<String, Rating>()

        for (i in 0..size - 1) {
            map.put(source.readString(), source.readParcelable<Parcelable>(Rating::class.java.classLoader) as Rating)
        }

        return map
    }

    fun writeRatingsMap(map: Map<String, Rating>?, dest: Parcel, flags: Int) {
        val size = map?.size ?: 0
        dest.writeInt(size)

        if (size == 0) {
            return
        }

        for (entry in map!!.entries) {
            dest.writeString(entry.key)
            dest.writeParcelable(entry.value, flags)
        }
    }

}
