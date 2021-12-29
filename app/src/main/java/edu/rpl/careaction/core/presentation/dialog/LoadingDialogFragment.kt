package edu.rpl.careaction.core.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import edu.rpl.careaction.R
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

object LoadingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            AlertDialog.Builder(it)
                .setView(requireActivity().layoutInflater.inflate(R.layout.dialog_loading, null))
                .setCancelable(false)
                .create()
        } ?: throw IllegalStateException(getString(R.string.activity_not_found_error_message))

}