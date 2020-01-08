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

@JsonClass(generateAdapter = false)
class TournamentMatch(
        @Json(name = "opponent") val opponent: AbsPlayer,
        @Json(name = "tournament") val tournament: AbsTournament,
        @Json(name = "result") val result: MatchResult
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return if (other is TournamentMatch) {
            opponent == other.opponent && tournament == other.tournament && result == other.result
        } else {
            false
        }
    }

    override fun hashCode(): Int = ObjectsCompat.hash(opponent, tournament, result)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeAbsPlayer(opponent, flags)
        dest.writeAbsTournament(tournament, flags)
        dest.writeParcelable(result, flags)
    }

    companion object {
        @JvmField
        val CREATOR = createParcel {
            TournamentMatch(
                    it.requireAbsPlayer(),
                    it.requireAbsTournament(),
                    it.requireParcelable(MatchResult::class.java.classLoader)
            )
        }

        val CHRONOLOGICAL_ORDER = Comparator<TournamentMatch> { o1, o2 ->
            SimpleDate.CHRONOLOGICAL_ORDER.compare(o1.tournament.date, o2.tournament.date)
        }

        val REVERSE_CHRONOLOGICAL_ORDER = Comparator<TournamentMatch> { o1, o2 ->
            CHRONOLOGICAL_ORDER.compare(o2, o1)
        }
    }

}
