package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.preferences.Preference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckablePreferenceView extends FrameLayout implements
        Preference.OnPreferenceChangeListener<Boolean>, View.OnClickListener {

    private static final int CHECKABLE_TYPE_CHECKBOX = 0;
    private static final int CHECKABLE_TYPE_SWITCH_COMPAT = 1;

    private CharSequence mDisabledDescriptionText;
    private CharSequence mEnabledDescriptionText;
    private CharSequence mTitleText;
    private int mCheckableType;
    private Preference<Boolean> mPreference;

    @BindView(R.id.icon)
    CompoundButton mCheckable;

    @BindView(R.id.description)
    TextView mDescription;

    @BindView(R.id.title)
    TextView mTitle;


    public CheckablePreferenceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
    }

    public CheckablePreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckablePreferenceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mPreference != null) {
            mPreference.addListener(this);
            refresh();
        }
    }

    @Override
    public void onClick(final View v) {
        if (mPreference == null) {
            return;
        }

        final Boolean value = mPreference.get();

        if (Boolean.TRUE.equals(value)) {
            mPreference.set(Boolean.FALSE);
        } else if (Boolean.FALSE.equals(value)) {
            mPreference.set(Boolean.TRUE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mPreference != null) {
            mPreference.removeListener(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        switch (mCheckableType) {
            case CHECKABLE_TYPE_CHECKBOX:
                layoutInflater.inflate(R.layout.view_checkbox_preference, this);
                break;

            case CHECKABLE_TYPE_SWITCH_COMPAT:
                layoutInflater.inflate(R.layout.view_switch_compat_preference, this);
                break;

            default:
                throw new RuntimeException("mCheckableType is an illegal value: " + mCheckableType);
        }

        ButterKnife.bind(this);
        setOnClickListener(this);

        if (isInEditMode()) {
            mTitle.setText(mTitleText);
            mDescription.setText(mEnabledDescriptionText);
        }

        if (TextUtils.isEmpty(mDisabledDescriptionText) || TextUtils.isEmpty(mEnabledDescriptionText)) {
            final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mTitle.setLayoutParams(layoutParams);
            mDescription.setVisibility(GONE);
        }
    }

    @Override
    public void onPreferenceChange(final Preference<Boolean> preference) {
        refresh();
    }

    private void parseAttributes(final AttributeSet attrs) {
        final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CheckablePreferenceView);
        mDisabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_disabledDescriptionText);
        mEnabledDescriptionText = ta.getText(R.styleable.CheckablePreferenceView_enabledDescriptionText);
        mTitleText = ta.getText(R.styleable.CheckablePreferenceView_titleText);
        mCheckableType = ta.getInt(R.styleable.CheckablePreferenceView_checkableType, CHECKABLE_TYPE_CHECKBOX);
        ta.recycle();
    }

    public void refresh() {
        if (mPreference == null) {
            mTitle.setText("");
            mDescription.setText("");
        } else {
            mPreference.addListener(this);
            mTitle.setText(mTitleText);

            if (Boolean.TRUE.equals(mPreference.get())) {
                mDescription.setText(mEnabledDescriptionText);
                mCheckable.setChecked(true);
            } else {
                mDescription.setText(mDisabledDescriptionText);
                mCheckable.setChecked(false);
            }
        }
    }

    public void set(@Nullable final Preference<Boolean> preference) {
        if (mPreference != null) {
            mPreference.removeListener(this);
        }

        mPreference = preference;
        refresh();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        mTitle.setEnabled(enabled);
        mDescription.setEnabled(enabled);
        mCheckable.setEnabled(enabled);
    }

}
