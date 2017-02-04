package com.garpr.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.misc.Timber;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimberEntryItemView extends LinearLayout implements BaseAdapterView<Timber.Entry> {

    private SparseIntArray mColors;

    @BindView(R.id.tvStackTrace)
    TextView mStackTrace;

    @BindView(R.id.tvTagAndMessage)
    TextView mTagAndMessage;


    public TimberEntryItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public TimberEntryItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimberEntryItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mColors = new SparseIntArray();
    }

    @Override
    public void setContent(final Timber.Entry content) {
        mTagAndMessage.setText(getResources().getString(R.string.tag_and_message, content.mTag,
                content.mMsg));

        if (TextUtils.isEmpty(content.mStackTrace)) {
            mStackTrace.setText("");
            mStackTrace.setVisibility(GONE);
        } else {
            mStackTrace.setText(content.mStackTrace);
            mStackTrace.setVisibility(VISIBLE);
        }

        int color = mColors.get(content.mColorAttrResId, -1);

        if (color == -1) {
            color = MiscUtils.getAttrColor(getContext(), content.mColorAttrResId);
            mColors.put(content.mColorAttrResId, color);
        }

        mTagAndMessage.setTextColor(color);
        mStackTrace.setTextColor(color);
    }

}
