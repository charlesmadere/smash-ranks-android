package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Ranking;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingItemView extends IdentityFrameLayout implements BaseAdapterView<Ranking>,
        View.OnClickListener {

    private NumberFormat mNumberFormat;
    private Ranking mContent;

    @BindView(R.id.tvName)
    TextView mName;

    @BindView(R.id.tvRank)
    TextView mRank;

    @BindView(R.id.tvRating)
    TextView mRating;


    public RankingItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public RankingItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RankingItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
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
        context.startActivity(PlayerActivity.getLaunchIntent(context, mContent));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
        mNumberFormat = NumberFormat.getNumberInstance();
    }

    @Override
    public void setContent(final Ranking content) {
        mContent = content;

        mRank.setText(mNumberFormat.format(mContent.getRank()));
        mName.setText(mContent.getName());
        mRating.setText(mContent.getRatingTruncated());

        refreshIdentity();
    }

}
