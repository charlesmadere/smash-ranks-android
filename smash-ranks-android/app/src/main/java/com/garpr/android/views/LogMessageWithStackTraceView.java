package com.garpr.android.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.models.LogMessage;


public class LogMessageWithStackTraceView extends LinearLayout {


    private int mDebugColor;
    private int mErrorColor;
    private int mWarnColor;
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
        initialize();
    }


    public LogMessageWithStackTraceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LogMessageWithStackTraceView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
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


    private void initialize() {
        final Context context = getContext();
        mDebugColor = ContextCompat.getColor(context, R.color.white);
        mErrorColor = ContextCompat.getColor(context, R.color.console_error);
        mWarnColor = ContextCompat.getColor(context, R.color.console_warn);
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
        mBody.setText(logMessage.getStackTrace());

        switch (mLogMessage.getPriority()) {
            case Log.DEBUG:
                mHeader.setTextColor(mDebugColor);
                mBody.setTextColor(mDebugColor);
                break;

            case Log.ERROR:
                mHeader.setTextColor(mErrorColor);
                mBody.setTextColor(mErrorColor);
                break;

            case Log.WARN:
                mHeader.setTextColor(mWarnColor);
                mBody.setText(mWarnColor);
                break;

            default:
                throw new RuntimeException("Unknown priority: " + mLogMessage.getPriority());
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(LogMessageWithStackTraceView.this);
        }


        public LogMessageWithStackTraceView getView() {
            return LogMessageWithStackTraceView.this;
        }



    }


}
