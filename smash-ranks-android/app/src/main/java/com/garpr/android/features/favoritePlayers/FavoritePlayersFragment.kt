package com.garpr.android.features.favoritePlayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.garpr.android.R
import com.garpr.android.data.models.FavoritePlayer
import com.garpr.android.extensions.appComponent
import com.garpr.android.extensions.layoutInflater
import com.garpr.android.extensions.viewModel
import com.garpr.android.features.common.fragments.BaseFragment
import com.garpr.android.misc.ListLayout
import com.garpr.android.misc.Refreshable
import kotlinx.android.synthetic.main.fragment_favorite_players.*

class FavoritePlayersFragment : BaseFragment(), ListLayout, Refreshable {

    private val adapter = Adapter()

    private val viewModel by lazy {
        viewModel(this) { appComponent.favoritePlayersViewModel }.value
    }


    companion object {
        fun create() = FavoritePlayersFragment()
    }

    private fun fetchFavoritePlayers() {
        viewModel.fetchFavoritePlayers()
    }

    override fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    private fun initListeners() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
            refreshState(it)
        })
    }

    private fun initViews() {
        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_favorite_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        fetchFavoritePlayers()
    }

    override fun refresh() {
        fetchFavoritePlayers()
    }

    private fun refreshState(state: FavoritePlayersViewModel.State) {
        if (state.favoritePlayers.isNullOrEmpty()) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            if (state.searchResults != null) {
                adapter.set(state.searchResults)
            } else {
                adapter.set(state.favoritePlayers)
            }

            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private class Adapter : RecyclerView.Adapter<FavoritePlayerViewHolder>() {

        private val list = mutableListOf<FavoritePlayer>()

        init {
            setHasStableIds(true)
        }

        internal fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemId(position: Int): Long {
            return list[position].hashCode().toLong()
        }

        override fun onBindViewHolder(holder: FavoritePlayerViewHolder, position: Int) {
            holder.favoritePlayerItemView.setContent(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlayerViewHolder {
            val inflater = parent.layoutInflater
            return FavoritePlayerViewHolder(inflater.inflate(R.layout.item_favorite_player, parent,
                    false))
        }

        internal fun set(list: List<FavoritePlayer>?) {
            this.list.clear()

            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }

            notifyDataSetChanged()
        }

    }

    private class FavoritePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val favoritePlayerItemView: FavoritePlayerItemView = itemView as FavoritePlayerItemView
    }

}
