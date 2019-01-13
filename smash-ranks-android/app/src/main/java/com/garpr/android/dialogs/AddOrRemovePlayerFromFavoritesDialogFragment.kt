package com.garpr.android.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.managers.FavoritePlayersManager
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.dialog_add_or_remove_player_from_favorites.*
import javax.inject.Inject

class AddOrRemovePlayerFromFavoritesDialogFragment : BaseBottomSheetDialogFragment(),
        FavoritePlayersManager.OnFavoritePlayersChangeListener, Refreshable {

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    private val player by lazy { arguments.requireParcelable<FavoritePlayer>(KEY_PLAYER) }


    companion object {
        const val TAG = "AddOrRemovePlayerFromFavoritesDialogFragment"
        private const val KEY_PLAYER = "Player"

        fun create(player: FavoritePlayer): AddOrRemovePlayerFromFavoritesDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_PLAYER, player)

            val fragment = AddOrRemovePlayerFromFavoritesDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_add_or_remove_player_from_favorites, container,
                false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritePlayersManager.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersManager: FavoritePlayersManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogPositiveText.setOnClickListener {
            if (player in favoritePlayersManager) {
                favoritePlayersManager.removePlayer(player)
            } else {
                favoritePlayersManager.addPlayer(player, player.region)
            }

            dismissAllowingStateLoss()
        }

        dialogNegativeText.setOnClickListener {
            dismissAllowingStateLoss()
        }

        favoritePlayersManager.addListener(this)
    }

    override fun refresh() {
        if (player in favoritePlayersManager) {
            dialogMessage.text = getString(R.string.remove_x_from_favorites, player.name)
            dialogPositiveText.setEndCompoundDrawableRelativeWithIntrinsicBounds(
                    R.drawable.ic_delete_white_24dp)
        } else {
            dialogMessage.text = getString(R.string.add_x_to_favorites, player.name)
            dialogPositiveText.setEndCompoundDrawableRelativeWithIntrinsicBounds(
                    R.drawable.ic_add_circle_white_24)
        }
    }

}
