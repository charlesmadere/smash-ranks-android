package com.garpr.android.misc

import android.text.Editable
import android.text.TextWatcher

abstract class AbstractTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        // intentionally empty, children can override
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // intentionally empty, children can override
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // intentionally empty, children can override
    }

}
