package edu.rpl.careaction.feature.user.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.core.utility.TextFieldUtility
import edu.rpl.careaction.databinding.FragmentUpdatePasswordBinding
import edu.rpl.careaction.feature.user.domain.dto.request.UpdatePasswordRequest
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.account.UpdatePasswordFormElement
import edu.rpl.careaction.feature.user.presentation.validation.account.UpdatePasswordFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class UpdatePasswordViewBindingFragment : ViewBindingFragment<FragmentUpdatePasswordBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentUpdatePasswordBinding::inflate

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextFieldEvent()
        initNavigationEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateFormValidationCallback())
    }

    private fun initTextFieldEvent() =
        TextFieldUtility.initOnTextFieldChangedErrorEvent(
            mapOf(
                binding.txtLayoutPassword to binding.txtFieldPassword,
                binding.txtLayoutNewPassword to binding.txtFieldNewPassword,
                binding.txtLayoutConfirmationPassword to binding.txtFieldConfirmationPassword,
            )
        )

    private fun initButtonEvent(
        updatePasswordFormValidation: UpdatePasswordFormValidation,
        formValidationCallback: FormValidationCallback<UpdatePasswordRequest>
    ) =
        binding.button.setOnClickListener {
            when (val validationResult = updatePasswordFormValidation.validated()) {
                is FormValidationResult.Success -> formValidationCallback.successCallback(
                    validationResult.validatedData
                )
                is FormValidationResult.Error -> formValidationCallback.errorCallback(
                    validationResult.message
                )
            }
        }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<ResponseBody, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.defaultUserUpdateSharedFlow.collect {
                    when (it) {
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                    }
                }
            }
        }

    private fun initNavigationEvent() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun generateFormValidation() =
        UpdatePasswordFormValidation(
            UpdatePasswordFormElement(
                binding.txtFieldNewPassword,
                binding.txtFieldPassword,
                binding.txtFieldConfirmationPassword
            )
        )

    private fun generateApiCallback(): ApiCallback<ResponseBody, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                binding.txtFieldPassword.text?.clear()
                binding.txtFieldNewPassword.text?.clear()
                binding.txtFieldConfirmationPassword.text?.clear()
            },
            loadingCallback = {
                DefaultApiCallbackUtility.loadingCallback(parentFragmentManager)
            },
            errorCallback = {
                DefaultApiCallbackUtility.errorCallback(
                    requireContext(),
                    findNavController(),
                    it.response
                )

                binding.txtFieldConfirmationPassword.text?.clear()
            }
        )

    private fun generateFormValidationCallback(): FormValidationCallback<UpdatePasswordRequest> =
        FormValidationCallback(
            successCallback = {
                userViewModel.updatePassword(it)
            },
            errorCallback = {
                it[R.id.txt_field_password]?.let { error ->
                    binding.txtLayoutPassword.error = getString(error.value)
                }

                it[R.id.txt_field_new_password]?.let { error ->
                    binding.txtLayoutNewPassword.error = getString(error.value)
                }

                it[R.id.txt_field_confirmation_password]?.let { error ->
                    binding.txtLayoutConfirmationPassword.error = getString(error.value)
                }
            }
        )

}