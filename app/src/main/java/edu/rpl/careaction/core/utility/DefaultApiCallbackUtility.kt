package edu.rpl.careaction.core.utility

import android.widget.Toast
import android.content.Context
import androidx.navigation.NavController
import androidx.fragment.app.FragmentManager
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.ApplicationNavGraphDirections
import edu.rpl.careaction.feature.page.error.ErrorParcelable
import edu.rpl.careaction.feature.page.loading.LoadingDialogFragment

class DefaultApiCallbackUtility {
    companion object {
        fun successCallback() =
            LoadingDialogFragment.dismiss()

        fun errorCallback(
            context: Context,
            errorNavController: NavController,
            errorResponse: ErrorResponse
        ) {
            LoadingDialogFragment.dismiss()
            errorResponse.throwable?.let {
                errorNavController.navigate(
                    ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                        ErrorParcelable(it)
                    )
                )
            } ?: Toast.makeText(context, errorResponse.message, Toast.LENGTH_SHORT).show()
        }

        fun loadingCallback(fragmentManager: FragmentManager) =
            LoadingDialogFragment.show(
                fragmentManager,
                String()
            )
    }
}