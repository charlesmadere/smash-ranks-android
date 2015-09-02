package com.garpr.android.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.garpr.android.R;
import com.garpr.android.calls.Regions;
import com.garpr.android.calls.ResponseOnUi;
import com.garpr.android.misc.Console;
import com.garpr.android.misc.ListUtils;
import com.garpr.android.misc.ListUtils.AlphabeticalSectionCreator;
import com.garpr.android.misc.ListUtils.AlphabeticallyComparable;
import com.garpr.android.misc.RecyclerAdapter;
import com.garpr.android.models.Region;
import com.garpr.android.views.CheckableItemView;
import com.garpr.android.views.SimpleSeparatorView;

import java.util.ArrayList;
import java.util.Collections;


public abstract class RegionsFragment extends BaseListFragment implements
        CheckableItemView.OnClickListener {


    private static final String KEY_REGIONS = "KEY_REGIONS";
    private static final String KEY_SELECTED_REGION = "KEY_SELECTED_REGION";
    private static final String TAG = "RegionsFragment";

    private ArrayList<ListItem> mListItems;
    private ArrayList<Region> mRegions;
    private boolean mPulled;
    protected FrameLayout mFrame;
    protected Region mSelectedRegion;




    @SuppressWarnings("unchecked")
    private void createListItems(final ArrayList<Region> regions) {
        mListItems = new ArrayList<>(regions.size());

        for (final Region region : regions) {
            mListItems.add(ListItem.createRegion(region));
        }

        final AlphabeticalSectionCreator creator = new AlphabeticalSectionCreator() {
            @Override
            public AlphabeticallyComparable createDigitSection() {
                return ListItem.createTitle(getString(R.string.pound_sign));
            }


            @Override
            public AlphabeticallyComparable createLetterSection(final String letter) {
                return ListItem.createTitle(letter);
            }


            @Override
            public AlphabeticallyComparable createOtherSection() {
                return ListItem.createTitle(getString(R.string.other));
            }
        };

        mListItems = (ArrayList<ListItem>) ListUtils.createAlphabeticalList(mListItems, creator);
        mListItems.trimToSize();
    }


    private void fetchRegions() {
        setLoading(true);

        final ResponseOnUi<ArrayList<Region>> response = new ResponseOnUi<ArrayList<Region>>(TAG, this) {
            @Override
            public void errorOnUi(final Exception e) {
                mPulled = false;
                Console.e(TAG, "Exception when retrieving regions!", e);
                showError();
            }


            @Override
            public void successOnUi(final ArrayList<Region> list) {
                mPulled = false;
                mRegions = list;
                prepareList();
            }
        };

        Regions.get(response, mPulled);
    }


    @Override
    protected void findViews() {
        super.findViews();
        final View view = getView();
        mFrame = (FrameLayout) view.findViewById(R.id.fragment_regions_list_frame);
    }


    @Override
    protected String getErrorText() {
        return getString(R.string.error_fetching_regions);
    }


    public Region getSelectedRegion() {
        return mSelectedRegion;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mRegions = savedInstanceState.getParcelableArrayList(KEY_REGIONS);
            mSelectedRegion = savedInstanceState.getParcelable(KEY_SELECTED_REGION);
        }

        if (mRegions == null || mRegions.isEmpty()) {
            fetchRegions();
        } else {
            prepareList();
        }
    }


    @Override
    public void onClick(final CheckableItemView v) {
        mSelectedRegion = (Region) v.getTag();
        notifyDataSetChanged();
    }


    @Override
    public void onRefresh() {
        super.onRefresh();

        if (!isLoading()) {
            mPulled = true;
            fetchRegions();
        }
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRegions != null && !mRegions.isEmpty()) {
            outState.putParcelableArrayList(KEY_REGIONS, mRegions);

            if (mSelectedRegion != null) {
                outState.putParcelable(KEY_SELECTED_REGION, mSelectedRegion);
            }
        }
    }


    private void prepareList() {
        Collections.sort(mRegions, Region.ALPHABETICAL_ORDER);
        createListItems(mRegions);
        setAdapter(new RegionsAdapter());
    }




    private static final class ListItem implements AlphabeticallyComparable {


        private Region mRegion;
        private String mTitle;
        private Type mType;


        private static ListItem createRegion(final Region region) {
            final ListItem listItem = new ListItem();
            listItem.mRegion = region;
            listItem.mType = Type.REGION;

            return listItem;
        }


        private static ListItem createTitle(final String title) {
            final ListItem listItem = new ListItem();
            listItem.mTitle = title;
            listItem.mType = Type.TITLE;

            return listItem;
        }


        @Override
        public boolean equals(final Object o) {
            final boolean isEqual;

            if (this == o) {
                isEqual = true;
            } else if (o instanceof ListItem) {
                final ListItem li = (ListItem) o;

                if (isRegion() && li.isRegion()) {
                    isEqual = mRegion.equals(li.mRegion);
                } else if (isTitle() && li.isTitle()) {
                    isEqual = mTitle.equals(li.mTitle);
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }

            return isEqual;
        }


        @Override
        public char getFirstCharOfName() {
            return toString().charAt(0);
        }


        private boolean isRegion() {
            return mType.equals(Type.REGION);
        }


        private boolean isTitle() {
            return mType.equals(Type.TITLE);
        }


        @Override
        public String toString() {
            final String title;

            switch (mType) {
                case REGION:
                    title = mRegion.getName();
                    break;

                case TITLE:
                    title = mTitle;
                    break;

                default:
                    throw new IllegalStateException("invalid ListItem Type: " + mType);
            }

            return title;
        }


        private enum Type {
            REGION, TITLE
        }


    }


    private final class RegionsAdapter extends RecyclerAdapter {


        private static final String TAG = "RegionsAdapter";


        private RegionsAdapter() {
            super(getRecyclerView());
        }


        @Override
        public String getAdapterName() {
            return TAG;
        }


        @Override
        public int getItemCount() {
            return mListItems.size();
        }


        @Override
        public int getItemViewType(final int position) {
            return mListItems.get(position).mType.ordinal();
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ListItem listItem = mListItems.get(position);

            switch (listItem.mType) {
                case REGION: {
                    final CheckableItemView.ViewHolder vh = (CheckableItemView.ViewHolder) holder;
                    final CheckableItemView civ = vh.getView();
                    civ.setText(listItem.mRegion.getName());
                    civ.setChecked(listItem.mRegion.equals(mSelectedRegion));
                    civ.setTag(listItem.mRegion);
                    break;
                }

                case TITLE: {
                    ((SimpleSeparatorView.ViewHolder) holder).getView().setText(listItem.mTitle);
                    break;
                }

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItem.mType);
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final ListItem.Type listItemType = ListItem.Type.values()[viewType];
            final RecyclerView.ViewHolder holder;

            switch (listItemType) {
                case REGION: {
                    final CheckableItemView civ = CheckableItemView.inflate(parent);
                    civ.setOnClickListener(RegionsFragment.this);
                    holder = civ.getViewHolder();
                    break;
                }

                case TITLE: {
                    holder = SimpleSeparatorView.inflate(parent).getViewHolder();
                    break;
                }

                default:
                    throw new RuntimeException("Unknown ListItem Type: " + listItemType);
            }

            return holder;
        }


    }


}
