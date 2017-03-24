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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.AbsTournament;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentItemView extends FrameLayout implements BaseAdapterView<AbsTournament>,
        View.OnClickListener {

    private AbsTournament mContent;

    @BindView(R.id.tvDate)
    TextView mDate;

    @BindView(R.id.tvName)
    TextView mName;


    public TournamentItemView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
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
        mDate.setText(mContent.getDate().getShortForm());
    }

}
