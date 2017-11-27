package com.garpr.android.views

import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.util.AttributeSet
import android.view.View
import com.garpr.android.App
import com.garpr.android.R
import com.garpr.android.activities.HomeActivity
import com.garpr.android.misc.Timber
import com.garpr.android.models.NightMode
import com.garpr.android.preferences.GeneralPreferenceStore
import com.garpr.android.preferences.Preference
import javax.inject.Inject

class ThemePreferenceView : SimplePreferenceView, DialogInterface.OnClickListener,
        Preference.OnPreferenceChangeListener<NightMode>, View.OnClickListener {

    @Inject
    protected lateinit var mGeneralPreferenceStore: GeneralPreferenceStore

    @Inject
    protected lateinit var mTimber: Timber


    companion object {
        private const val TAG = "ThemePreferenceView"
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            return
        }

        mGeneralPreferenceStore.nightMode.addListener(this)
        refresh()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        dialog.dismiss()

        val current = mGeneralPreferenceStore.nightMode.get()
        val selected = NightMode.values()[which]

        if (current == selected) {
            return
        }

        showRestartDialog(DialogInterface.OnDismissListener {
            mTimber.d(TAG, "theme was \"$current\", is now \"$selected\"")
            mGeneralPreferenceStore.nightMode.set(selected)
            refresh()

            context.startActivity(HomeActivity.getLaunchIntent(context))
        })
    }

    override fun onClick(v: View) {
        val items = arrayOfNulls<CharSequence>(NightMode.values().size)

        for (i in 0 until NightMode.values().size) {
            items[i] = resources.getText(NightMode.values()[i].textResId)
        }

        val current = mGeneralPreferenceStore.nightMode.get()
        val checkedItem = current?.ordinal ?: -1

        AlertDialog.Builder(context)
                .setSingleChoiceItems(items, checkedItem, this)
                .setTitle(R.string.theme)
                .show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mGeneralPreferenceStore.nightMode.removeListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (!isInEditMode) {
            App.get().appComponent.inject(this)
        }

        setOnClickListener(this)
        titleText = resources.getText(R.string.theme)

        if (isInEditMode) {
            descriptionText = resources.getText(R.string.auto)
        } else {
            mGeneralPreferenceStore.nightMode.addListener(this)
            refresh()
        }
    }

    override fun onPreferenceChange(preference: Preference<NightMode>) {
        if (isAlive) {
            refresh()
        }
    }

    override fun refresh() {
        super.refresh()

        val nightMode = mGeneralPreferenceStore.nightMode.get()
        descriptionText = resources.getText(nightMode?.textResId ?: R.string.not_yet_set)
    }

    private fun showRestartDialog(onDismissListener: DialogInterface.OnDismissListener) {
        AlertDialog.Builder(context)
                .setMessage(R.string.the_app_will_now_restart)
                .setNeutralButton(R.string.ok, null)
                .setOnDismissListener(onDismissListener)
                .show()
    }

}
