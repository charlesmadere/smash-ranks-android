package com.garpr.android.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.garpr.android.BuildConfig;
import com.garpr.android.R;

public class BuildInfoTextView extends AppCompatTextView {

    public BuildInfoTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public BuildInfoTextView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setText(getResources().getString(R.string.build_info_format, BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE));
    }

}
