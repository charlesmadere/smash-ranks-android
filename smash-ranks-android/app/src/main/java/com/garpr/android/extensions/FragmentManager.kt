package com.garpr.android.extensions

import androidx.fragment.app.FragmentManager
import com.garpr.android.data.models.AbsPlayer
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.data.models.Region
import com.garpr.android.features.favoritePlayers.AddOrRemovePlayerFromFavoritesDialogFragment

fun FragmentManager.showAddOrRemoveFavoritePlayerDialog(player: AbsPlayer, region: Region) {
    val favoritePlayer = FavoritePlayer(
            id = player.id,
            name = player.name,
            region = region
    )

    val dialog = AddOrRemovePlayerFromFavoritesDialogFragment.create(favoritePlayer)
    dialog.show(this, AddOrRemovePlayerFromFavoritesDialogFragment.TAG)
}
