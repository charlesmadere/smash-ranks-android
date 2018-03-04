package com.garpr.android.misc

import com.garpr.android.misc.PlayerToolbarManager.Presentation
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
