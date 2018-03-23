package com.garpr.android.managers

import com.garpr.android.managers.PlayerToolbarManager.Presentation
import com.garpr.android.models.MatchResult
import com.garpr.android.models.MatchesBundle

class PlayerToolbarManagerImpl : PlayerToolbarManager {

    override fun getPresentation(matchesBundle: MatchesBundle?, matchResult: MatchResult?): Presentation {
        var presentation = Presentation()

        if (matchesBundle?.matches?.isNotEmpty() == true) {
            presentation = presentation.copy(
                    isFilterVisible = true,
                    isFilterAllVisible = matchResult != null,
                    isFilterLossesVisible = matchResult != MatchResult.LOSE,
                    isFilterWinsVisible = matchResult != MatchResult.WIN
            )
        }

        return presentation
    }

}
