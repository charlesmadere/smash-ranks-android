package com.garpr.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.models.WinsLosses;

import java.text.NumberFormat;

public class WinsLossesView extends AppCompatTextView implements BaseAdapterView<WinsLosses> {

    private NumberFormat mNumberFormat;
    private Paint mLossesPaint;
    private Paint mWinsPaint;
    private Rect mLossesRect;
    private Rect mWinsRect;
    private WinsLosses mContent;


    public WinsLossesView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WinsLossesView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void calculateRects() {
        if (!ViewCompat.isAttachedToWindow(this) || !ViewCompat.isLaidOut(this) || mContent == null) {
            mLossesRect.setEmpty();
            mWinsRect.setEmpty();
            return;
        }

        final int height = getHeight();
        final int width = getWidth();

        final int barThickness = height / 4;
        final int top = (height - barThickness) / 2;
        final int bottom = top + barThickness;

        if (mContent.mLosses == 0 && mContent.mWins == 0) {
            mLossesRect.setEmpty();
            mWinsRect.setEmpty();
        } else if (mContent.mLosses == 0) {
            mLossesRect.setEmpty();
            mWinsRect.set(0, top, width, bottom);
        } else if (mContent.mWins == 0) {
            mLossesRect.set(0, top, width, bottom);
            mWinsRect.setEmpty();
        } else {
            final float winsPercent = (float) mContent.mWins / (float) (mContent.mLosses + mContent.mWins);
            final int start = Math.round(winsPercent * (float) width);
            mLossesRect.set(start, top, width, bottom);
            mWinsRect.set(0, top, start, bottom);
        }
    }

    private void initialize() {
        mLossesPaint = new Paint();
        mLossesPaint.setAntiAlias(true);
        mLossesPaint.setColor(ContextCompat.getColor(getContext(), R.color.lose));
        mLossesPaint.setDither(true);
        mLossesPaint.setStyle(Paint.Style.FILL);

        mWinsPaint = new Paint();
        mWinsPaint.setAntiAlias(true);
        mWinsPaint.setColor(ContextCompat.getColor(getContext(), R.color.win));
        mWinsPaint.setDither(true);
        mWinsPaint.setStyle(Paint.Style.FILL);

        mLossesRect = new Rect();
        mWinsRect = new Rect();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        calculateRects();
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!mLossesRect.isEmpty()) {
            canvas.drawRect(mLossesRect, mLossesPaint);
        }

        if (!mWinsRect.isEmpty()) {
            canvas.drawRect(mWinsRect, mWinsPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNumberFormat = NumberFormat.getInstance();

        if (isInEditMode()) {
            setContent(new WinsLosses(8, 5));
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right,
            final int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateRects();
        invalidate();
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateRects();
        invalidate();
    }

    @Override
    public void setContent(final WinsLosses content) {
        mContent = content;

        calculateRects();
        setText(getResources().getString(R.string.x_em_dash_y, mNumberFormat.format(content.mWins),
                mNumberFormat.format(content.mLosses)));
        invalidate();
    }

}
