package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
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

import com.garpr.android.App;
import com.garpr.android.R;
import com.garpr.android.activities.TournamentActivity;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.RegionManager;
import com.garpr.android.models.AbsTournament;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TournamentDividerView extends FrameLayout implements BaseAdapterView<AbsTournament>,
        View.OnClickListener {

    private AbsTournament mContent;

    @Inject
    RegionManager mRegionManager;

    @BindView(R.id.tvDate)
    TextView mDate;

    @BindView(R.id.tvName)
    TextView mName;


    public TournamentDividerView(@NonNull final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public TournamentDividerView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TournamentDividerView(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AbsTournament getContent() {
        return mContent;
    }

    @Override
    public void onClick(final View v) {
        final Context context = getContext();
        final Activity activity = MiscUtils.optActivity(context);

        if (activity instanceof OnClickListener) {
            ((OnClickListener) activity).onClick(this);
        } else {
            context.startActivity(TournamentActivity.Companion.getLaunchIntent(context, mContent,
                    mRegionManager.getRegion(context)));
        }
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
    public void setContent(final AbsTournament content) {
        mContent = content;

        mName.setText(mContent.getName());
        mDate.setText(mContent.getDate().getMediumForm());
    }


    public interface OnClickListener {
        void onClick(final TournamentDividerView v);
    }

}
