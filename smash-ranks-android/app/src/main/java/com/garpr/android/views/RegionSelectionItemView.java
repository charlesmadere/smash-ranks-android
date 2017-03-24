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
import com.garpr.android.models.Region;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionSelectionItemView extends LinearLayout implements BaseAdapterView<Region>,
        View.OnClickListener {

    private Listeners mListeners;
    private Region mContent;

    @BindView(R.id.radioButton)
    RadioButton mRadioButton;

    @BindView(R.id.tvId)
    TextView mId;

    @BindView(R.id.tvDisplayName)
    TextView mDisplayName;


    public RegionSelectionItemView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionSelectionItemView(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegionSelectionItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void attach() {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            mListeners = (Listeners) activity;
        } else {
            mListeners = null;
        }
    }

    public Region getContent() {
        return mContent;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attach();
    }

    @Override
    public void onClick(final View v) {
        if (mListeners != null) {
            mListeners.onClick(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mListeners = null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
        attach();
    }

    @Override
    public void setContent(final Region content) {
        mContent = content;

        mDisplayName.setText(mContent.getDisplayName());
        mId.setText(mContent.getId());
        mRadioButton.setChecked(mContent.getId().equalsIgnoreCase(mListeners == null ? null :
                mListeners.getSelectedRegion()));
    }


    public interface Listeners {
        @Nullable
        String getSelectedRegion();

        void onClick(final RegionSelectionItemView v);
    }

}
