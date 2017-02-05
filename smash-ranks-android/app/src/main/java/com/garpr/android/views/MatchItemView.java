package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Match;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MatchItemView extends LinearLayout implements BaseAdapterView<Match>,
        View.OnClickListener {

    private Match mContent;

    @BindView(R.id.tvOpponentName)
    TextView mOpponentName;

    @BindView(R.id.tvTournamentDate)
    TextView mTournamentDate;

    @BindView(R.id.tvTournamentName)
    TextView mTournamentName;


    public MatchItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchItemView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
    }

    @OnClick(R.id.llTournament)
    void onTournamentClick() {
        final Context context = getContext();
        context.startActivity(TournamentActivity.getLaunchIntent(context, mContent));
    }

    @Override
    public void setContent(final Match content) {
        mContent = content;

        mOpponentName.setText(mContent.getOpponentName());
        mTournamentName.setText(mContent.getTournamentName());
        mTournamentDate.setText(mContent.getTournamentDate().getDateString());
    }

}
