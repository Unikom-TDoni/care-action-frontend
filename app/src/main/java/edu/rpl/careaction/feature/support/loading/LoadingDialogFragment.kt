package edu.rpl.careaction.feature.support.loading

import android.app.Dialog
import android.os.Bundle
import edu.rpl.careaction.R
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.content.DialogInterface
import androidx.fragment.app.FragmentManager


object LoadingDialogFragment : DialogFragment() {

    private var shown = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            AlertDialog.Builder(it)
                .setView(requireActivity().layoutInflater.inflate(R.layout.dialog_loading, null))
                .setCancelable(false)
                .create()
        } ?: throw IllegalStateException(getString(R.string.activity_not_found_error_message))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Translucent_NoTitleBar
        )
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (shown) return
        super.show(manager, tag)
        shown = true
    }

    override fun onDismiss(dialog: DialogInterface) {
        shown = false
        super.onDismiss(dialog)
    }
}