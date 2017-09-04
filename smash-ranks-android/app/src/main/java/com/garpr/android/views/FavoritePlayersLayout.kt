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

    lateinit private var mAdapter: FavoritePlayersAdapter

    @Inject
    lateinit protected var mFavoritePlayersManager: FavoritePlayersManager

    private val mRecyclerView: RecyclerView by bindView(R.id.recyclerView)
    private val mEmpty: View by bindView(R.id.empty)


    companion object {
        fun inflate(parent: ViewGroup): FavoritePlayersLayout {
            val inflater = LayoutInflater.from(parent.context)
            return inflater.inflate(R.layout.layout_favorite_players, parent, false)
                    as FavoritePlayersLayout
        }
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

        mFavoritePlayersManager.addListener(this)
        refresh()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mFavoritePlayersManager.addListener(this)
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

        mRecyclerView.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        mRecyclerView.setHasFixedSize(true)
        mAdapter = FavoritePlayersAdapter(context)
        mRecyclerView.adapter = mAdapter
        mFavoritePlayersManager.addListener(this)

        refresh()
    }

    override val recyclerView: RecyclerView?
        get() = mRecyclerView

    override fun refresh() {
        if (mFavoritePlayersManager.isEmpty) {
            mAdapter.clear()
            mRecyclerView.visibility = View.GONE
            mEmpty.visibility = View.VISIBLE
        } else {
            mAdapter.set(mFavoritePlayersManager.absPlayers)
            mEmpty.visibility = View.GONE
            mRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun search(query: String?) {
        mThreadUtils.run(object : ThreadUtils.Task {
            private var mList: List<AbsPlayer>? = null

            override fun onBackground() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mList = ListUtils.searchPlayerList(query, mFavoritePlayersManager.absPlayers)
            }

            override fun onUi() {
                if (!isAlive || !TextUtils.equals(query, searchQuery)) {
                    return
                }

                mAdapter.set(mList)
            }
        })
    }

}
