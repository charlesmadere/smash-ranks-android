package com.garpr.android.features.favoritePlayers

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.extensions.requireParcelable
import com.garpr.android.features.common.fragments.dialogs.BaseBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_or_remove_player_from_favorites.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddOrRemovePlayerFromFavoritesDialogFragment : BaseBottomSheetDialogFragment() {

    private val player by lazy { arguments.requireParcelable<FavoritePlayer>(KEY_PLAYER) }

    private val viewModel: AddOrRemovePlayerFromFavoritesViewModel by viewModel()

    private val addOrRemovePlayerClickListener = View.OnClickListener { view ->
        viewModel.addOrRemoveFromFavorites()
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        dismissAllowingStateLoss()
    }

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

    private fun initListeners() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        dialogPositiveText.setOnClickListener(addOrRemovePlayerClickListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initialize(player)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_add_or_remove_player_from_favorites, container,
                false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initViews()
    }

    private fun refreshState(state: AddOrRemovePlayerFromFavoritesViewModel.State) {
        if (state.isFetching) {
            dialogMessage.visibility = View.INVISIBLE
            dialogPositiveText.visibility = View.INVISIBLE
            dialogProgressGroup.visibility = View.VISIBLE
        } else {
            if (state.isFavorited) {
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

            dialogMessage.visibility = View.VISIBLE
            dialogPositiveText.visibility = View.VISIBLE
            dialogProgressGroup.visibility = View.GONE
        }
    }

}
