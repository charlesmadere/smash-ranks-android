package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.activities.HeadToHeadActivity;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.FullTournament;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentMatchItemView extends IdentityFrameLayout implements
        BaseAdapterView<FullTournament.Match>, DialogInterface.OnClickListener,
        View.OnClickListener {

    private FullTournament.Match mContent;

    @BindView(R.id.tvLoserName)
    TextView mLoserName;

    @BindView(R.id.tvWinnerName)
    TextView mWinnerName;


    public TournamentMatchItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentMatchItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentMatchItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        final Context context = getContext();

        switch (which) {
            case 0:
                context.startActivity(PlayerActivity.getLaunchIntent(context,
                        mContent.getWinnerId(), mContent.getWinnerName()));
                break;

            case 1:
                context.startActivity(PlayerActivity.getLaunchIntent(context,
                        mContent.getLoserId(), mContent.getLoserName()));
                break;

            case 2:
                context.startActivity(HeadToHeadActivity.getLaunchIntent(context, mContent));
                break;

            default:
                throw new RuntimeException("illegal which: " + which);
        }
    }

    @Override
    public void onClick(final View v) {
        final CharSequence[] items = {
                mContent.getWinnerName(),
                mContent.getLoserName(),
                getResources().getString(R.string.x_vs_y, mContent.getWinnerName(),
                        mContent.getLoserName())
        };

        new AlertDialog.Builder(getContext())
                .setItems(items, this)
                .setTitle(R.string.view)
                .show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    protected void refreshIdentity() {
        if (mIdentityManager.isId(mContent.getWinnerId())) {
            styleTextViewForUser(mWinnerName);
            styleTextViewForSomeoneElse(mLoserName);
            identityIsUser();
        } else if (mIdentityManager.isId(mContent.getLoserId())) {
            styleTextViewForSomeoneElse(mWinnerName);
            styleTextViewForUser(mLoserName);
            identityIsUser();
        } else {
            styleTextViewForSomeoneElse(mWinnerName);
            styleTextViewForUser(mLoserName);
            identityIsSomeoneElse();
        }
    }

    @Override
    public void setContent(final FullTournament.Match content) {
        mContent = content;

        mLoserName.setText(content.getLoserName());
        mWinnerName.setText(content.getWinnerName());

        refreshIdentity();
    }

}
