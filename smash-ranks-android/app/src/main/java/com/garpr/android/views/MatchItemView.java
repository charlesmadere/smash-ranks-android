package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.Match;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchItemView extends FrameLayout implements BaseAdapterView<Match>,
        View.OnClickListener {

    private Match mContent;

    @BindView(R.id.tvName)
    TextView mName;


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

    public Match getContent() {
        return mContent;
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        final Activity activity = MiscUtils.optActivity(context);

        if (activity instanceof OnClickListener) {
            ((OnClickListener) activity).onClick(this);
        } else {
            context.startActivity(PlayerActivity.getLaunchIntent(context, mContent.getOpponentId(),
                    mContent.getOpponentName()));
        }
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

        mName.setText(mContent.getOpponentName());

        switch (mContent.getResult()) {
            case EXCLUDED:
                mName.setTextColor(MiscUtils.getAttrColor(getContext(),
                        android.R.attr.textColorSecondary));
                break;

            case LOSE:
                mName.setTextColor(ContextCompat.getColor(getContext(), R.color.lose));
                break;

            case WIN:
                mName.setTextColor(ContextCompat.getColor(getContext(), R.color.win));
                break;

            default:
                throw new RuntimeException("unknown result: " + mContent.getResult());
        }
    }


    public interface OnClickListener {
        void onClick(final MatchItemView v);
    }

}
