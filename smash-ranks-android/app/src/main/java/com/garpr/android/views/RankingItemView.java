package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.Ranking;

import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingItemView extends IdentityFrameLayout implements BaseAdapterView<Ranking>,
        View.OnClickListener, View.OnLongClickListener {

    private NumberFormat mNumberFormat;
    private Ranking mContent;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    RegionManager mRegionManager;

    @Nullable
    @BindView(R.id.previousRankView)
    PreviousRankView mPreviousRankView;

    @BindView(R.id.tvName)
    TextView mName;

    @BindView(R.id.tvRank)
    TextView mRank;

    @BindView(R.id.tvRating)
    TextView mRating;


    public RankingItemView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RankingItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankingItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getIdentityId() {
        return mContent.getId();
    }

    @Override
    protected void identityIsSomeoneElse() {
        super.identityIsSomeoneElse();
        styleTextViewForSomeoneElse(mName);
    }

    @Override
    protected void identityIsUser() {
        super.identityIsUser();
        styleTextViewForUser(mName);
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(PlayerActivity.Companion.getLaunchIntent(context, mContent,
                mRegionManager.getRegion(context)));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
        setOnLongClickListener(this);
        mNumberFormat = NumberFormat.getNumberInstance();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
    }

    @Override
    public boolean onLongClick(final View v) {
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(getContext(),
                mContent.getPlayer(), mRegionManager.getRegion(getContext()));
    }

    @Override
    public void setContent(final Ranking content) {
        mContent = content;

        if (mPreviousRankView != null) {
            mPreviousRankView.setContent(mContent);
        }

        mRank.setText(mNumberFormat.format(mContent.getRank()));
        mName.setText(mContent.getName());
        mRating.setText(mContent.getRatingTruncated());

        refreshIdentity();
    }

}
