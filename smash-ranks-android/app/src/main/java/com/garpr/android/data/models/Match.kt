package com.garpr.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.core.util.ObjectsCompat
import com.garpr.android.extensions.createParcel
import com.garpr.android.extensions.requireAbsPlayer
import com.garpr.android.extensions.requireAbsTournament
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.extensions.writeAbsPlayer
import com.garpr.android.extensions.writeAbsTournament
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Comparator

@JsonClass(generateAdapter = false)
class Match(
        @Json(name = "result") result: MatchResult,
        @Json(name = "opponent") val opponent: AbsPlayer,
        @Json(name = "tournament") val tournament: AbsTournament
) : AbsMatch(
        result
), Parcelable {

    companion object {
        @JvmField
        val CREATOR = createParcel {
            Match(
                    it.requireParcelable(MatchResult::class.java.classLoader),
                    it.requireAbsPlayer(),
                    it.requireAbsTournament()
            )
        }

        val CHRONOLOGICAL_ORDER = Comparator<Match> { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.tournament.date, o2.tournament.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<Match> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (super.equals(other)) {
            if (other is Match) {
                opponent == other.opponent && tournament == other.tournament
            } else {
                true
            }
        } else {
            false
        }
    }

    override fun hashCode(): Int = ObjectsCompat.hash(result, opponent, tournament)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsTournament(tournament, flags)
    }

}
