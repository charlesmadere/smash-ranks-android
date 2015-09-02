package com.garpr.android.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garpr.android.BuildConfig;
import com.garpr.android.R;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.models.LogMessage;


public class ConsoleActivity extends BaseToolbarListActivity {


    private static final String TAG = "ConsoleActivity";

    private MenuItem mClearLog;




    public static void start(final Activity activity) {
        final Intent intent = new Intent(activity, ConsoleActivity.class);
        activity.startActivity(intent);
    }


    private void createAdapter() {
        final ConsoleAdapter adapter;

        if (BuildConfig.DEBUG) {
            adapter = new DebugConsoleAdapter();
        } else {
            adapter = new ReleaseConsoleAdapter();
        }

        setAdapter(adapter);
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getOptionsMenu() {
        return R.menu.activity_console;
    }


    @Override
    protected void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        if (Console.hasLogMessages()) {
            mClearLog.setEnabled(true);
        } else {
            mClearLog.setEnabled(false);
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAdapter();
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_console_menu_clear_log:
                mClearLog.setEnabled(false);
                Console.clearLogMessages();
                notifyDataSetChanged();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mClearLog = menu.findItem(R.id.activity_console_menu_clear_log);

        if (Console.hasLogMessages()) {
            mClearLog.setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        notifyDataSetChanged();
        setLoading(false);
    }


    @Override
    protected void prepareViews() {
        super.prepareViews();

        final RecyclerView recyclerView = getRecyclerView();
        recyclerView.setVerticalScrollBarEnabled(false);

        final Resources resources = getResources();
        final int topAndBottom = resources.getDimensionPixelSize(R.dimen.root_padding_half);
        final int start = ViewCompat.getPaddingStart(recyclerView);
        final int end = ViewCompat.getPaddingEnd(recyclerView);
        ViewCompat.setPaddingRelative(recyclerView, start, topAndBottom, end, topAndBottom);

        recyclerView.requestLayout();
    }


    @Override
    protected boolean showDrawerIndicator() {
        return false;
    }




    private abstract class ConsoleAdapter extends RecyclerAdapter {


        private final int mDebugTextColor;
        private final int mErrorTextColor;
        private final int mWarnTextColor;


        private ConsoleAdapter() {
            super(getRecyclerView());

            mDebugTextColor = getColorCompat(R.color.white);
            mErrorTextColor = getColorCompat(R.color.console_error);
            mWarnTextColor = getColorCompat(R.color.console_warn);
        }


        protected void formatTextViewPerLevel(final LogMessage logMessage, final TextView textView) {
            switch (logMessage.getPriority()) {
                case Log.DEBUG:
                    textView.setTextColor(mDebugTextColor);
                    break;

                case Log.ERROR:
                    textView.setTextColor(mErrorTextColor);
                    break;

                case Log.WARN:
                    textView.setTextColor(mWarnTextColor);
                    break;

                default:
                    throw new RuntimeException("Unknown priority: " + logMessage.getPriority());
            }
        }


        @Override
        public int getItemCount() {
            return Console.getLogMessagesSize();
        }


        @Override
        public long getItemId(final int position) {
            return Console.getLogMessage(position).getId();
        }


        @Override
        public abstract int getItemViewType(final int position);


    }


    private final class DebugConsoleAdapter extends ConsoleAdapter {


        private static final String TAG = "DebugConsoleAdapter";


        @Override
        public String getAdapterName() {
            return TAG;
        }


        @Override
        public int getItemViewType(final int position) {
            final LogMessage logMessage = Console.getLogMessage(position);
            final int viewType;

            if (logMessage.isThrowable()) {
                viewType = LogMessageWithStackTraceViewHolder.VIEW_TYPE;
            } else {
                viewType = LogMessageViewHolder.VIEW_TYPE;
            }

            return viewType;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final int viewType = getItemViewType(position);
            final LogMessage logMessage = Console.getLogMessage(position);
            final Spanned tagAndMessage = Html.fromHtml(getString(R.string.x_bold_colon_y,
                    logMessage.getTag(), logMessage.getMessage()));

            switch (viewType) {
                case LogMessageViewHolder.VIEW_TYPE:
                    final LogMessageViewHolder lmvh = (LogMessageViewHolder) holder;
                    lmvh.mTagAndMessage.setText(tagAndMessage);
                    formatTextViewPerLevel(logMessage, lmvh.mTagAndMessage);
                    break;

                case LogMessageWithStackTraceViewHolder.VIEW_TYPE:
                    final LogMessageWithStackTraceViewHolder lmwstvh =
                            (LogMessageWithStackTraceViewHolder) holder;
                    lmwstvh.mTagAndMessage.setText(tagAndMessage);
                    lmwstvh.mStackTrace.setText(logMessage.getStackTrace());
                    formatTextViewPerLevel(logMessage, lmwstvh.mTagAndMessage);
                    formatTextViewPerLevel(logMessage, lmwstvh.mStackTrace);
                    break;

                default:
                    throw new RuntimeException("Unknown viewType: " + viewType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final LayoutInflater inflater = getLayoutInflater();

            final View view;
            final RecyclerView.ViewHolder holder;

            switch (viewType) {
                case LogMessageViewHolder.VIEW_TYPE:
                    view = inflater.inflate(R.layout.model_log_message, parent, false);
                    holder = new LogMessageViewHolder(view);
                    break;

                case LogMessageWithStackTraceViewHolder.VIEW_TYPE:
                    view = inflater.inflate(R.layout.model_log_message_with_stack_trace, parent, false);
                    holder = new LogMessageWithStackTraceViewHolder(view);
                    break;

                default:
                    throw new RuntimeException("Unknown viewType: " + viewType);
            }

            return holder;
        }


    }


    private final class ReleaseConsoleAdapter extends ConsoleAdapter {


        private static final String TAG = "ReleaseConsoleAdapter";


        @Override
        public String getAdapterName() {
            return TAG;
        }


        @Override
        public int getItemViewType(final int position) {
            return LogMessageViewHolder.VIEW_TYPE;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final LogMessage logMessage = Console.getLogMessage(position);
            final String text;

            if (logMessage.isThrowable()) {
                text = getString(R.string.x_bold_colon_y_paren_z, logMessage.getTag(),
                        logMessage.getMessage(), logMessage.getThrowableMessage());
            } else {
                text = getString(R.string.x_bold_colon_y, logMessage.getTag(),
                        logMessage.getMessage());
            }

            final LogMessageViewHolder lmvh = (LogMessageViewHolder) holder;
            lmvh.mTagAndMessage.setText(Html.fromHtml(text));
            formatTextViewPerLevel(logMessage, lmvh.mTagAndMessage);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            final LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.model_log_message, parent, false);
            return new LogMessageViewHolder(view);
        }


    }


    private static final class LogMessageViewHolder extends RecyclerView.ViewHolder {


        private static final int VIEW_TYPE = 1;
        private final TextView mTagAndMessage;


        private LogMessageViewHolder(final View view) {
            super(view);
            mTagAndMessage = (TextView) view.findViewById(R.id.model_log_message_tag_and_message);
        }


    }


    private static final class LogMessageWithStackTraceViewHolder extends RecyclerView.ViewHolder {


        private static final int VIEW_TYPE = 2;
        private final TextView mStackTrace;
        private final TextView mTagAndMessage;


        private LogMessageWithStackTraceViewHolder(final View view) {
            super(view);
            mStackTrace = (TextView) view.findViewById(R.id.model_log_message_with_stack_trace_stack_trace);
            mTagAndMessage = (TextView) view.findViewById(R.id.model_log_message_with_stack_trace_tag_and_message);
        }


    }


}
