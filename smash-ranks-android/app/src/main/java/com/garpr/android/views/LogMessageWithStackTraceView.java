package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.LogMessage;


public class LogMessageWithStackTraceView extends LinearLayout {


    private LogMessage mLogMessage;
    private TextView mBody;
    private TextView mHeader;
    private ViewHolder mViewHolder;




    public static LogMessageWithStackTraceView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (LogMessageWithStackTraceView) inflater.inflate(
                R.layout.view_log_message_with_stack_trace, parent, false);
    }


    public LogMessageWithStackTraceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    public LogMessageWithStackTraceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogMessageWithStackTraceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public LogMessage getLogMessage() {
        return mLogMessage;
    }


    public ViewHolder getViewHolder() {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
        }

        return mViewHolder;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBody = (TextView) findViewById(R.id.view_log_message_with_stack_trace_body);
        mHeader = (TextView) findViewById(R.id.view_log_message_with_stack_trace_header);
    }


    public void setLogMessage(final LogMessage logMessage) {
        mLogMessage = logMessage;
        mHeader.setText(Html.fromHtml(getResources().getString(R.string.x_bold_colon_y,
                mLogMessage.getTag(), mLogMessage.getMessage())));
        mBody.setText(mLogMessage.getStacktrace());

        final int textColor = mLogMessage.getPriority().getColor();
        mHeader.setTextColor(textColor);
        mBody.setTextColor(textColor);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        protected ViewHolder() {
            super(LogMessageWithStackTraceView.this);
        }


        public LogMessageWithStackTraceView getView() {
            return LogMessageWithStackTraceView.this;
        }



    }


}
