package com.garpr.android.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.garpr.android.adapters.BaseAdapterView;

public class MessageItemView extends AppCompatTextView implements BaseAdapterView<String> {

    public MessageItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setContent(final String content) {
        setText(content);
    }

}
