package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.Match;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchItemView extends IdentityFrameLayout implements BaseAdapterView<Match>,
        View.OnClickListener, View.OnLongClickListener {

    private Match mContent;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @BindView(R.id.tvName)
    TextView mName;


    public MatchItemView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MatchItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Match getContent() {
        return mContent;
    }

    @Override
    protected String getIdentityId() {
        return mContent.getOpponent().getId();
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
        final Activity activity = MiscUtils.optActivity(context);

        if (activity instanceof OnClickListener) {
            ((OnClickListener) activity).onClick(this);
        } else {
            context.startActivity(PlayerActivity.getLaunchIntent(context,
                    mContent.getOpponent().getId(), mContent.getOpponent().getName()));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
        setOnLongClickListener(this);

        if (isInEditMode()) {
            return;
        }

        App.get().getAppComponent().inject(this);
    }

    @Override
    public boolean onLongClick(final View v) {
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(getContext(),
                mContent.getOpponent());
    }

    @Override
    public void setContent(final Match content) {
        mContent = content;

        mName.setText(mContent.getOpponent().getName());

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

        refreshIdentity();
    }


    public interface OnClickListener {
        void onClick(final MatchItemView v);
    }

}
