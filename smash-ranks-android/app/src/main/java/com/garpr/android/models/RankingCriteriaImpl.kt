package com.garpr.android.models

import android.os.Parcel
import android.os.Parcelable
import com.garpr.android.extensions.*
import com.google.gson.annotations.SerializedName

data class RankingCriteriaImpl(
        @SerializedName("activeTF") override val activeTf: Boolean = true,
        @SerializedName("ranking_activity_day_limit") override val rankingActivityDayLimit: Int? = null,
        @SerializedName("ranking_num_tourneys_attended") override val rankingNumTourneysAttended: Int? = null,
        @SerializedName("tournament_qualified_day_limit") override val tournamentQualifiedDayLimit: Int? = null,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("id") val id: String
) : Parcelable, RankingCriteria {

    companion object {
        @JvmField
        val CREATOR = createParcel { RankingCriteriaImpl(it.readBoolean(), it.readInteger(),
                it.readInteger(), it.readInteger(), it.readString(), it.readString()) }
    }

    override val rankingCriteriaKind: RankingCriteria.Kind
        get() = RankingCriteria.Kind.IMPL

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(activeTf)
        dest.writeInteger(rankingNumTourneysAttended)
        dest.writeInteger(rankingNumTourneysAttended)
        dest.writeInteger(tournamentQualifiedDayLimit)
        dest.writeString(displayName)
        dest.writeString(id)
    }

}
