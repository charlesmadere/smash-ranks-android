package com.garpr.android.fragments;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garpr.android.R;

import butterknife.Bind;
import butterknife.OnClick;


public class WelcomeFragment extends BaseFragment {


    private static final String TAG = "WelcomeFragment";

    private AccelerateDecelerateInterpolator mAnimationInterpolator;
    private Listener mListener;

    @Bind(R.id.fragment_welcome_next)
    protected ImageButton mNext;

    @Bind(R.id.fragment_welcome_orb)
    protected ImageView mOrb;

    @Bind(R.id.fragment_welcome_content)
    protected LinearLayout mContent;

    @Bind(R.id.fragment_welcome_gar_pr)
    protected TextView mGarPr;

    @Bind(R.id.fragment_welcome_text)
    protected TextView mWelcomeText;




    public static WelcomeFragment create() {
        return new WelcomeFragment();
    }


    private ValueAnimator[] createAnimators(final View... views) {
        final ValueAnimator[] animators = new ValueAnimator[views.length];

        for (int i = 0; i < views.length; ++i) {
            final View v = views[i];

            animators[i] = ValueAnimator.ofFloat(0f, 1f);
            animators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    v.setAlpha((Float) animation.getAnimatedValue());
                }
            });

            animators[i].setInterpolator(mAnimationInterpolator);
            animators[i].setDuration(500L);
        }

        return animators;
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_welcome;
    }


    @Override
    public String getFragmentName() {
        return TAG;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareViews();
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }


    @OnClick(R.id.fragment_welcome_next)
    protected void onNextClick() {
        mListener.onWelcomeNextClick();
    }


    @SuppressWarnings("deprecation")
    private void prepareViews() {
        mAnimationInterpolator = new AccelerateDecelerateInterpolator();
        final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.indigo));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContent.setBackground(background);
        } else {
            mContent.setBackgroundDrawable(background);
        }

        final ValueAnimator animator = ValueAnimator.ofInt(255, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                background.setAlpha((Integer) animation.getAnimatedValue());
            }
        });

        animator.setInterpolator(mAnimationInterpolator);
        animator.setDuration(4000L);
        animator.start();

        mWelcomeText.setText(Html.fromHtml(getString(R.string.gar_pr_welcome_text)));

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(createAnimators(mOrb, mGarPr, mWelcomeText, mNext));
        animatorSet.start();
    }




    public interface Listener {


        void onWelcomeNextClick();


    }


}
