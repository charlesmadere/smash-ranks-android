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
import android.widget.TextView;

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.PlayerActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.FavoritePlayersManager;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.AbsPlayer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerItemView extends IdentityFrameLayout implements BaseAdapterView<AbsPlayer>,
        View.OnClickListener, View.OnLongClickListener {

    private AbsPlayer mContent;

    @Inject
    FavoritePlayersManager mFavoritePlayersManager;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.tvName)
    TextView mName;


    public PlayerItemView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerItemView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected AbsPlayer getIdentity() {
        return mContent;
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
        context.startActivity(PlayerActivity.Companion.getLaunchIntent(context, mContent,
                mRegionManager.getRegion(context)));
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
        return mFavoritePlayersManager.showAddOrRemovePlayerDialog(getContext(), mContent,
                mRegionManager.getRegion(getContext()));
    }

    @Override
    public void setContent(final AbsPlayer content) {
        mContent = content;

        mName.setText(mContent.getName());

        refreshIdentity();
    }

}
