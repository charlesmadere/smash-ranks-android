package com.garpr.android.activities;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.garpr.android.BuildConfig;
import com.garpr.android.R;
import com.garpr.android.misc.Console;
import com.garpr.android.models.LogMessage;
import com.garpr.android.views.LogMessageView;
import com.garpr.android.views.LogMessageWithStackTraceView;


public class ConsoleActivity extends BaseToolbarListActivity {


    private static final String TAG = "ConsoleActivity";

    private MenuItem mClearLog;




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

        if (BuildConfig.DEBUG) {
            setAdapter(new DebugConsoleAdapter());
        } else {
            setAdapter(new ConsoleAdapter());
        }
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




    private class ConsoleAdapter extends RecyclerView.Adapter {


        @Override
        public int getItemCount() {
            return Console.getLogMessagesSize();
        }


        @Override
        public long getItemId(final int position) {
            return Console.getLogMessage(position).getId();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final LogMessage logMessage = Console.getLogMessage(position);
            ((LogMessageView.ViewHolder) holder).getView().setLogMessage(logMessage);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            return LogMessageView.inflate(parent).getViewHolder();
        }


    }


    private final class DebugConsoleAdapter extends ConsoleAdapter {


        private static final int VIEW_TYPE_SIMPLE = 0;
        private static final int VIEW_TYPE_THROWABLE = 1;


        @Override
        public int getItemViewType(final int position) {
            final LogMessage logMessage = Console.getLogMessage(position);

            if (logMessage.isThrowable()) {
                return VIEW_TYPE_THROWABLE;
            } else {
                return VIEW_TYPE_SIMPLE;
            }
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == VIEW_TYPE_THROWABLE) {
                final LogMessage logMessage = Console.getLogMessage(position);
                ((LogMessageWithStackTraceView.ViewHolder) holder).getView()
                        .setLogMessage(logMessage);
            } else {
                super.onBindViewHolder(holder, position);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            if (viewType == VIEW_TYPE_THROWABLE) {
                return LogMessageWithStackTraceView.inflate(parent).getViewHolder();
            } else {
                return super.onCreateViewHolder(parent, viewType);
            }
        }


    }


    public static class IntentBuilder extends BaseActivity.IntentBuilder {


        public IntentBuilder(final Context context) {
            super(context, ConsoleActivity.class);
        }


    }


}
