package com.garpr.android.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.garpr.android.R
import com.garpr.android.extensions.putOptionalCharSequence
import com.garpr.android.extensions.requireCharSequence
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_to_favorites.*

class BasicBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var negativeButtonClickListener: ((DialogFragment) -> Unit)? = null
    var positiveButtonClickListener: ((DialogFragment) -> Unit)? = null

    private val message by lazy { arguments.requireCharSequence(KEY_MESSAGE) }
    private val negativeButton by lazy { arguments?.getCharSequence(KEY_NEGATIVE_BUTTON) }
    private val positiveButton by lazy { arguments?.getCharSequence(KEY_POSITIVE_BUTTON) }
    private val title by lazy { arguments?.getCharSequence(KEY_TITLE) }


    companion object {
        private const val KEY_MESSAGE = "Message"
        private const val KEY_NEGATIVE_BUTTON = "NegativeButton"
        private const val KEY_POSITIVE_BUTTON = "PositiveButton"
        private const val KEY_TITLE = "Title"

        fun create(message: CharSequence,
                title: CharSequence? = null,
                negativeButton: CharSequence? = null,
                positiveButton: CharSequence? = null
        ): BasicBottomSheetDialogFragment {
            val args = Bundle()
            args.putCharSequence(KEY_MESSAGE, message)
            args.putOptionalCharSequence(KEY_TITLE, title)
            args.putOptionalCharSequence(KEY_NEGATIVE_BUTTON, negativeButton)
            args.putOptionalCharSequence(KEY_POSITIVE_BUTTON, positiveButton)

            val fragment = BasicBottomSheetDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_add_to_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!title.isNullOrBlank()) {
            dialogTitle.text = title
            dialogTitle.visibility = View.VISIBLE
        }

        dialogMessage.text = message

        if (positiveButton != null) {
            dialogPositiveButton.text = positiveButton
            dialogPositiveButton.visibility = View.VISIBLE

            dialogPositiveButton.setOnClickListener {
                positiveButtonClickListener?.invoke(this)
                dismiss()
            }
        }

        if (negativeButton != null) {
            dialogNegativeButton.text = negativeButton
            dialogNegativeButton.visibility = View.VISIBLE

            dialogNegativeButton.setOnClickListener {
                negativeButtonClickListener?.invoke(this)
                dismiss()
            }
        }
    }
}
