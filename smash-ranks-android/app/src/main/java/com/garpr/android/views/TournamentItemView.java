package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.AbsTournament;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentItemView extends LinearLayout implements BaseAdapterView<AbsTournament>,
        View.OnClickListener {

    private AbsTournament mContent;

    @BindView(R.id.tvDate)
    TextView mDate;

    @BindView(R.id.tvName)
    TextView mName;


    public TournamentItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentItemView(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(TournamentActivity.getLaunchIntent(context, mContent));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    public void setContent(final AbsTournament content) {
        mContent = content;

        mName.setText(mContent.getName());
        mDate.setText(mContent.getDate().getRelativeDateTimeText(getContext()));
    }

}
