package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.AbsPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerSelectionItemView extends LinearLayout implements BaseAdapterView<AbsPlayer>,
        View.OnClickListener {

    private AbsPlayer mContent;
    private Listeners mListeners;

    @BindView(R.id.radioButton)
    RadioButton mRadioButton;

    @BindView(R.id.tvName)
    TextView mName;


    public PlayerSelectionItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerSelectionItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerSelectionItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AbsPlayer getContent() {
        return mContent;
    }

    @Nullable
    private AbsPlayer getSelectedPlayer() {
        return mListeners == null ? null : mListeners.getSelectedPlayer();
    }

    @Override
    public void onClick(final View v) {
        if (mListeners != null) {
            mListeners.onClick(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);

        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            mListeners = (Listeners) activity;
        } else {
            mListeners = null;
        }
    }

    @Override
    public void setContent(final AbsPlayer content) {
        mContent = content;

        mName.setText(mContent.getName());
        mRadioButton.setChecked(mContent.equals(getSelectedPlayer()));
    }


    public interface Listeners {
        @Nullable
        AbsPlayer getSelectedPlayer();

        void onClick(final PlayerSelectionItemView v);
    }

}