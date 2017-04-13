package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.misc.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErrorLinearLayout extends LinearLayout {

    private CharSequence mOriginalLine2;

    @BindView(R.id.recyclerViewLine1)
    TextView mLine1;

    @BindView(R.id.recyclerViewLine2)
    TextView mLine2;


    public ErrorLinearLayout(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorLinearLayout(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ErrorLinearLayout(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mOriginalLine2 = mLine2.getText();
    }

    public void setVisibility(final int visibility, final int errorCode) {
        super.setVisibility(visibility);

        if (errorCode == Constants.ERROR_CODE_BAD_REQUEST) {
            mLine2.setText(R.string.region_mismatch_error);
        } else {
            mLine2.setText(mOriginalLine2);
        }
    }

}
