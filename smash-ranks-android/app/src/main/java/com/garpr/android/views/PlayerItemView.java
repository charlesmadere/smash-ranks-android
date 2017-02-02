package com.garpr.android.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.AbsPlayer;

public class PlayerItemView extends AppCompatTextView implements BaseAdapterView<AbsPlayer>,
        View.OnClickListener {

    private AbsPlayer mContent;


    public PlayerItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        context.startActivity(PlayerActivity.getLaunchIntent(context, mContent));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
    }

    @Override
    public void setContent(final AbsPlayer content) {
        mContent = content;

        setText(mContent.getName());
    }

}
