package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.HeadToHeadActivity;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.FullTournament;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentMatchItemView extends IdentityFrameLayout implements
        BaseAdapterView<FullTournament.Match>, DialogInterface.OnClickListener,
        View.OnClickListener {

    private FullTournament.Match mContent;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.tvLoserName)
    TextView mLoserName;

    @BindView(R.id.tvWinnerName)
    TextView mWinnerName;


    public TournamentMatchItemView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentMatchItemView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentMatchItemView(@NonNull final Context context,
            @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr,
            @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        final Context context = getContext();

        switch (which) {
            case 0:
                context.startActivity(PlayerActivity.getLaunchIntent(context,
                        mContent.getWinnerId(), mContent.getWinnerName(),
                        mRegionManager.getRegion(context)));
                break;

            case 1:
                context.startActivity(PlayerActivity.getLaunchIntent(context,
                        mContent.getLoserId(), mContent.getLoserName(),
                        mRegionManager.getRegion(context)));
                break;

            case 2:
                context.startActivity(HeadToHeadActivity.Companion.getLaunchIntent(context,
                        mContent));
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
                getResources().getText(R.string.head_to_head)
        };

        new AlertDialog.Builder(getContext())
                .setItems(items, this)
                .setTitle(R.string.view)
                .show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
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
            styleTextViewForSomeoneElse(mLoserName);
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
