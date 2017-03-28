package com.garpr.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.garpr.android.R;
import com.garpr.android.adapters.BaseAdapterView;
import com.garpr.android.misc.MiscUtils;
import com.garpr.android.models.Region;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionSelectionItemView extends LinearLayout implements BaseAdapterView<Region>,
        View.OnClickListener {

    private Region mContent;

    @BindView(R.id.radioButton)
    RadioButton mRadioButton;

    @BindView(R.id.tvId)
    TextView mId;

    @BindView(R.id.tvDisplayName)
    TextView mDisplayName;


    public RegionSelectionItemView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionSelectionItemView(final Context context, @Nullable final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RegionSelectionItemView(final Context context, final AttributeSet attrs,
            final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Region getContent() {
        return mContent;
    }

    @Override
    public void onClick(final View v) {
        final Activity activity = MiscUtils.optActivity(getContext());

        if (activity instanceof Listeners) {
            ((Listeners) activity).onClick(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    @Override
    public void setContent(final Region content) {
        mContent = content;

        mDisplayName.setText(mContent.getDisplayName());
        mId.setText(mContent.getId());

        final Activity activity = MiscUtils.optActivity(getContext());
        if (activity instanceof Listeners) {
            mRadioButton.setChecked(mContent.getId().equalsIgnoreCase(
                    ((Listeners) activity).getSelectedRegion()));
        } else {
            mRadioButton.setChecked(false);
        }
    }


    public interface Listeners {
        @Nullable
        String getSelectedRegion();

        void onClick(final RegionSelectionItemView v);
    }

}
