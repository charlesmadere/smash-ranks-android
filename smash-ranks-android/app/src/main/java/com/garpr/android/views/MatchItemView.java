package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.Match;

import butterknife.ButterKnife;

public class MatchItemView extends LinearLayout implements BaseAdapterView<Match>,
        View.OnClickListener {

    private Match mContent;


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

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    public void setContent(final Match content) {
        mContent = content;


    }

}
