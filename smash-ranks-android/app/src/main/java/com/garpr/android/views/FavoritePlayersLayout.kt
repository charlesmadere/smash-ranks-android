package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.adapters.FavoritePlayersAdapter
import com.garpr.android.misc.FavoritePlayersManager
import com.garpr.android.misc.ListUtils
import com.garpr.android.misc.ThreadUtils
import com.garpr.android.models.AbsPlayer
import kotterknife.bindView
import javax.inject.Inject

class FavoritePlayersLayout : SearchableFrameLayout,
        FavoritePlayersManager.OnFavoritePlayersChangeListener {

    private lateinit var adapter: FavoritePlayersAdapter

    @Inject
    protected lateinit var favoritePlayersManager: FavoritePlayersManager

    private val empty: View by bindView(R.id.empty)


    companion object {
        fun inflate(parent: ViewGroup): FavoritePlayersLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_favorite_players, parent, false) as FavoritePlayersLayout
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        favoritePlayersManager.addListener(this)
        refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        favoritePlayersManager.addListener(this)
    }

    override fun onFavoritePlayersChanged(manager: FavoritePlayersManager) {
        if (isAlive) {
            refresh()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (isInEditMode) {
            return
        }

        App.get().appComponent.inject(this)

        recyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        adapter = FavoritePlayersAdapter(context)
        recyclerView.adapter = adapter
        favoritePlayersManager.addListener(this)

        refresh()
    }

    override val recyclerView: RecyclerView by bindView(R.id.recyclerView)

    override fun refresh() {
        if (favoritePlayersManager.isEmpty) {
            adapter.clear()
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            adapter.set(favoritePlayersManager.absPlayers)
            empty.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun search(query: String?) {
        threadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, favoritePlayersManager.absPlayers)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                adapter.set(mList)
            }
        })
    }

}
