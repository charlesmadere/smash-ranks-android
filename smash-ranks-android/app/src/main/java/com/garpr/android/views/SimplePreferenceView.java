package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garpr.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimplePreferenceView extends FrameLayout {

    private CharSequence mDescriptionText;
    private CharSequence mTitleText;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.title)
    TextView mTitle;


    public SimplePreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
    }

    public SimplePreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimplePreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.view_simple_preference, this);

        ButterKnife.bind(this);

        mTitle.setText(mTitleText);
        mDescription.setText(mDescriptionText);
    }

    private void parseAttributes(final AttributeSet attrs) {
        final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SimplePreferenceView);
        mDescriptionText = ta.getText(R.styleable.SimplePreferenceView_simpleDescriptionText);
        mTitleText = ta.getText(R.styleable.SimplePreferenceView_simpleTitleText);
        ta.recycle();
    }

    public void setDescriptionText(@Nullable final CharSequence descriptionText) {
        mDescriptionText = descriptionText;
        mDescription.setText(mDescriptionText);
    }

    public void setDescriptionText(@StringRes final int descriptionTextResId) {
        setDescriptionText(getResources().getText(descriptionTextResId));
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        mTitle.setEnabled(enabled);
        mDescription.setEnabled(enabled);
    }

    public void setTitleText(@Nullable final CharSequence titleText) {
        mTitleText = titleText;
        mTitle.setText(mTitleText);
    }

    public void setTitleText(@StringRes final int titleTextResId) {
        setTitleText(getResources().getText(titleTextResId));
    }

}
