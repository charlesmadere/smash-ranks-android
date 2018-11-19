package com.garpr.android.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.garpr.android.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_add_to_favorites.*

class BasicBottomSheetDialog(
        context: Context,
        private val message: CharSequence,
        private val title: CharSequence? = null,
        private val negativeButton: Button? = null,
        private val positiveButton: Button? = null
) : BottomSheetDialog(context, R.style.BottomSheetDialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_to_favorites)

        if (!title.isNullOrBlank()) {
            dialogTitle.text = title
            dialogTitle.visibility = View.VISIBLE
        }

        dialogMessage.text = message

        if (positiveButton != null) {
            dialogPositiveButton.text = positiveButton.text
            dialogPositiveButton.visibility = View.VISIBLE

            dialogPositiveButton.setOnClickListener {
                positiveButton.listener?.invoke(Unit)
                dismiss()
            }
        }

        if (negativeButton != null) {
            dialogNegativeButton.text = negativeButton.text
            dialogNegativeButton.visibility = View.VISIBLE

            dialogNegativeButton.setOnClickListener {
                negativeButton.listener?.invoke(Unit)
                dismiss()
            }
        }
    }

    class Button(
            internal val text: CharSequence,
            internal val listener: ((Unit) -> Unit)? = null
    )

}
