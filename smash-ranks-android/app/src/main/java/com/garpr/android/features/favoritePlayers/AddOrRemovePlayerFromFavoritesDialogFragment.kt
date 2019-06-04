package com.garpr.android.features.favoritePlayers

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.features.base.BaseBottomSheetDialogFragment
import com.garpr.android.misc.Refreshable
import com.garpr.android.repositories.FavoritePlayersRepository
import kotlinx.android.synthetic.main.dialog_add_or_remove_player_from_favorites.*
import javax.inject.Inject

class AddOrRemovePlayerFromFavoritesDialogFragment : BaseBottomSheetDialogFragment(),
        FavoritePlayersRepository.OnFavoritePlayersChangeListener, Refreshable {

    @Inject
    protected lateinit var favoritePlayersRepository: FavoritePlayersRepository

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
        favoritePlayersRepository.removeListener(this)
    }

    override fun onFavoritePlayersChange(favoritePlayersRepository: FavoritePlayersRepository) {
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
            if (player in favoritePlayersRepository) {
                favoritePlayersRepository.removePlayer(player)
            } else {
                favoritePlayersRepository.addPlayer(player, player.region)
            }

            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            dismissAllowingStateLoss()
        }

        favoritePlayersRepository.addListener(this)
    }

    override fun refresh() {
        if (player in favoritePlayersRepository) {
            dialogMessage.text = getString(R.string.remove_x_from_favorites, player.name)
            dialogPositiveText.setText(R.string.yes_remove)
            dialogPositiveText.setStartCompoundDrawableRelativeWithIntrinsicBounds(
                    R.drawable.ic_delete_white_24dp)
        } else {
            dialogMessage.text = getString(R.string.add_x_to_favorites, player.name)
            dialogPositiveText.setText(R.string.yes_add)
            dialogPositiveText.setStartCompoundDrawableRelativeWithIntrinsicBounds(
                    R.drawable.ic_add_circle_white_24dp)
        }
    }

}
