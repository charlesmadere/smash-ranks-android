package com.garpr.android.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;

import com.garpr.android.R;
import com.garpr.android.fragments.PlayersFragment;
import com.garpr.android.fragments.RegionsFragment;
import com.garpr.android.fragments.ToolbarRegionsFragment;
import com.garpr.android.fragments.WelcomeFragment;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.Constants;
import com.garpr.android.misc.CrashlyticsManager;
import com.garpr.android.models.Player;
import com.garpr.android.models.Region;
import com.garpr.android.settings.Settings;
import com.garpr.android.settings.Settings.User;
import com.garpr.android.views.NonSwipeableViewPager;


public class OnboardingActivity extends BaseActivity implements PlayersFragment.Listeners,
        ToolbarRegionsFragment.NextListener, WelcomeFragment.Listener {


    private static final int ONBOARDING_FRAGMENT_COUNT = 3;
    private static final int ONBOARDING_FRAGMENT_PLAYERS = 2;
    private static final int ONBOARDING_FRAGMENT_REGIONS = 1;
    private static final int ONBOARDING_FRAGMENT_WELCOME = 0;
    private static final String KEY_SELECTED_REGION = "KEY_SELECTED_REGION";
    private static final String TAG = "OnboardingActivity";

    private NonSwipeableViewPager mPager;
    private PlayersFragment mPlayersFragment;
    private Region mSelectedRegion;
    private RegionsFragment mRegionsFragment;




    private void findViews() {
        mPager = (NonSwipeableViewPager) findViewById(R.id.activity_onboarding_pager);
    }


    private void finishOnboarding(final boolean savePlayer) {
        if (savePlayer) {
            final Player player = mPlayersFragment.getSelectedPlayer();
            User.Player.set(player);
        }

        Settings.OnboardingComplete.set(true);
        RankingsActivity.start(this);
        finish();
    }


    @Override
    public String getActivityName() {
        return TAG;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_onboarding;
    }


    private void nextOnboardingStep() {
        final int currentPagerItem = mPager.getCurrentItem();

        switch (currentPagerItem) {
            case ONBOARDING_FRAGMENT_REGIONS:
                final Region region = mRegionsFragment.getSelectedRegion();

                if (!region.equals(mSelectedRegion)) {
                    mSelectedRegion = region;
                    Settings.Region.set(mSelectedRegion);
                    User.Region.set(mSelectedRegion);
                    mPlayersFragment.refresh();
                }

                mPager.setCurrentItem(ONBOARDING_FRAGMENT_PLAYERS, true);
                break;

            case ONBOARDING_FRAGMENT_WELCOME:
                mPager.setCurrentItem(ONBOARDING_FRAGMENT_REGIONS, true);
                break;

            default:
                Console.w(TAG, "Illegal currentPagerItem in nextOnboardingStep(): " + currentPagerItem);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (mPager == null || mPager.getCurrentItem() != ONBOARDING_FRAGMENT_PLAYERS) {
            super.onBackPressed();
        } else if (!mPlayersFragment.onBackPressed()) {
            mPager.setCurrentItem(ONBOARDING_FRAGMENT_REGIONS, true);
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean onboardingAlreadyCompleted = Settings.OnboardingComplete.get();
        CrashlyticsManager.setBool(Constants.ONBOARDING_ALREADY_COMPLETED,
                onboardingAlreadyCompleted);

        if (onboardingAlreadyCompleted) {
            RankingsActivity.start(this);
            finish();
        } else {
            findViews();
            mPager.setAdapter(new OnboardingFragmentAdapter());
        }
    }


    @Override
    public void onGoClick() {
        final Player player = mPlayersFragment.getSelectedPlayer();

        new AlertDialog.Builder(this)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(getString(R.string.youre_x_from_y_ready, player.getName(),
                        mSelectedRegion.getName()))
                .setPositiveButton(R.string.lets_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        finishOnboarding(true);
                    }
                })
                .show();
    }


    @Override
    public void onRegionNextClick() {
        nextOnboardingStep();
    }


    @Override
    @SuppressWarnings("NullableProblems")
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (!savedInstanceState.isEmpty()) {
            mSelectedRegion = savedInstanceState.getParcelable(KEY_SELECTED_REGION);
        }
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedRegion != null) {
            outState.putParcelable(KEY_SELECTED_REGION, mSelectedRegion);
        }
    }


    @Override
    public void onSkipClick() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure_you_dont_want_to_select_your_tag)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                        finishOnboarding(false);
                    }
                })
                .show();
    }


    @Override
    public void onWelcomeNextClick() {
        nextOnboardingStep();
    }




    private final class OnboardingFragmentAdapter extends FragmentPagerAdapter {


        private OnboardingFragmentAdapter() {
            super(getSupportFragmentManager());
        }


        @Override
        public int getCount() {
            return ONBOARDING_FRAGMENT_COUNT;
        }


        @Override
        public Fragment getItem(final int position) {
            final Fragment fragment;

            switch (position) {
                case ONBOARDING_FRAGMENT_WELCOME:
                    fragment = WelcomeFragment.create();
                    break;

                case ONBOARDING_FRAGMENT_REGIONS:
                    fragment = ToolbarRegionsFragment.create();
                    break;

                case ONBOARDING_FRAGMENT_PLAYERS:
                    fragment = PlayersFragment.create();
                    break;

                default:
                    // this should never happen
                    throw new RuntimeException("Invalid position: " + position);
            }

            return fragment;
        }


        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Object item = super.instantiateItem(container, position);

            switch (position) {
                case ONBOARDING_FRAGMENT_PLAYERS:
                    mPlayersFragment = (PlayersFragment) item;
                    break;

                case ONBOARDING_FRAGMENT_REGIONS:
                    mRegionsFragment = (RegionsFragment) item;
                    break;
            }

            return item;
        }


    }


}
