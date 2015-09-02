package com.garpr.android.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garpr.android.R;

import java.text.NumberFormat;


public class MatchResultsItemView extends TextView {


    private int mBarHeight;
    private int mLoses;
    private int mWins;
    private Paint mLosesPaint;
    private Paint mWinsPaint;
    private Rect mLosesRect;
    private Rect mWinsRect;
    private ViewHolder mViewHolder;




    public static MatchResultsItemView inflate(final ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return (MatchResultsItemView) inflater.inflate(R.layout.view_match_results_item, parent, false);
    }


    public MatchResultsItemView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }


    private void calculateRects() {
        final int height = getHeight(), width = getWidth();

        if (height == 0 || width == 0) {
            mLosesRect.setEmpty();
            mWinsRect.setEmpty();
            return;
        }

        final int top = (height - mBarHeight) / 2;
        final int bottom = top + mBarHeight;

        if (mLoses == 0 && mWins == 0) {
            mLosesRect.setEmpty();
            mWinsRect.setEmpty();
        } else if (mLoses == 0) {
            mLosesRect.setEmpty();
            mWinsRect.set(0, top, width, bottom);
        } else if (mWins == 0) {
            mLosesRect.set(0, top, width, top + mBarHeight);
            mWinsRect.setEmpty();
        } else {
            final float winsPercent = (float) mWins / (float) (mLoses + mWins);
            final int left = Math.round(winsPercent * width);
            mLosesRect.set(left, top, width, bottom);
            mWinsRect.set(0, top, left, bottom);
        }
    }


    public int getLoses() {
        return mLoses;
    }


    public int getWins() {
        return mWins;
    }


    public ViewHolder getViewHolder() {
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
        }

        return mViewHolder;
    }


    private void initialize() {
        final Context context = getContext();
        mBarHeight = getResources().getDimensionPixelSize(R.dimen.match_results_bar_height);

        mLosesRect = new Rect();
        mLosesPaint = new Paint();
        mLosesPaint.setAntiAlias(true);
        mLosesPaint.setColor(ContextCompat.getColor(context, R.color.transparent_lose_pink));
        mLosesPaint.setStyle(Paint.Style.FILL);

        mWinsRect = new Rect();
        mWinsPaint = new Paint();
        mWinsPaint.setAntiAlias(true);
        mWinsPaint.setColor(ContextCompat.getColor(context, R.color.transparent_win_green));
        mWinsPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        if (!mLosesRect.isEmpty()) {
            canvas.drawRect(mLosesRect, mLosesPaint);
        }

        if (!mWinsRect.isEmpty()) {
            canvas.drawRect(mWinsRect, mWinsPaint);
        }

        super.onDraw(canvas);
    }


    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateRects();
        invalidate();
    }


    public void setResults(final int[] results) {
        setResults(results[0], results[1]);
    }


    public void setResults(int loses, int wins) {
        if (loses < 0) {
            loses = 0;
        }

        if (wins < 0) {
            wins = 0;
        }

        mLoses = loses;
        mWins = wins;

        calculateRects();

        final NumberFormat nf = NumberFormat.getInstance();
        setText(getResources().getString(R.string.x_em_dash_y, nf.format(mWins),
                nf.format(mLoses)));
    }




    public final class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder() {
            super(MatchResultsItemView.this);
        }


        public MatchResultsItemView getView() {
            return MatchResultsItemView.this;
        }


    }


}
