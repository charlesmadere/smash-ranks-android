package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.garpr.android.R;
import com.garpr.android.misc.MiscUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SearchLayout extends LinearLayout {

    @Nullable
    private Listeners mListeners;

    @BindView(R.id.searchField)
    EditText mField;

    @BindView(R.id.searchClear)
    ImageButton mClear;


    public SearchLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchLayout(@NonNull final Context context, @Nullable final AttributeSet attrs,
            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void close() {
        final int visibility = getVisibility();

        if (visibility != VISIBLE) {
            return;
        }

        setVisibility(GONE);
        MiscUtils.closeKeyboard(mField);
        refreshClearVisibility();

        if (mListeners != null) {
            mListeners.onSearchFieldClosed(this);
        }
    }

    public void expand() {
        final int visibility = getVisibility();

        if (visibility == VISIBLE) {
            return;
        }

        setVisibility(VISIBLE);
        MiscUtils.openKeyboard(mField);
        refreshClearVisibility();

        if (mListeners != null) {
            mListeners.onSearchFieldExpanded(this);
        }
    }

    @Nullable
    public String getText() {
        final CharSequence text = mField.getText();

        if (TextUtils.isEmpty(text)) {
            return null;
        } else {
            return text.toString().trim();
        }
    }

    public boolean isExpanded() {
        return getVisibility() == VISIBLE;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.searchField)
    void onFieldTextChanged() {
        refreshClearVisibility();

        if (mListeners != null) {
            mListeners.onSearchFieldTextChanged(this);
        }
    }

    private void refreshClearVisibility() {
        if (TextUtils.isEmpty(getText())) {
            mClear.setVisibility(GONE);
        } else {
            mClear.setVisibility(VISIBLE);
        }
    }

    public void setListeners(@Nullable final Listeners listeners) {
        mListeners = listeners;
    }


    public interface Listeners {
        void onSearchFieldClosed(final SearchLayout searchLayout);
        void onSearchFieldExpanded(final SearchLayout searchLayout);
        void onSearchFieldTextChanged(final SearchLayout searchLayout);
    }

}
