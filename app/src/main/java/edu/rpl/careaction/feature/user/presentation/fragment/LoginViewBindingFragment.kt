package edu.rpl.careaction.feature.user.presentation.fragment


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.module.presentation.SpanLinkBuilder
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.core.utility.TextFieldUtility
import edu.rpl.careaction.databinding.FragmentLoginBinding
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.feature.user.presentation.validation.login.LoginFormElement
import edu.rpl.careaction.feature.user.presentation.validation.login.LoginFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewBindingFragment : ViewBindingFragment<FragmentLoginBinding>() {

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.landing_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextViewLink()
        initTextFieldEvent()
        initNavigationEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateFormValidationCallback())
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<User, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userSharedFlow.collect {
                    when (it) {
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Success -> apiCallback.successCallback(it)
                    }
                }
            }
        }

    private fun initButtonEvent(
        loginFormValidation: LoginFormValidation,
        validationCallback: FormValidationCallback<LoginRequest>
    ) {
        binding.btnLogin.setOnClickListener {
            when (val validationResult = loginFormValidation.validated()) {
                is FormValidationResult.Success -> validationCallback.successCallback(
                    validationResult.validatedData
                )
                is FormValidationResult.Error -> validationCallback.errorCallback(validationResult.message)
            }
        }
    }

    private fun initTextFieldEvent() =
        TextFieldUtility.initOnTextFieldChangedCustomErrorEvent(
            mapOf(
                binding.txtLayoutEmail to binding.txtFieldEmail,
                binding.txtLayoutPassword to binding.txtFieldPassword,
            )
        )

    private fun initNavigationEvent() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initTextViewLink() {
        binding.txtViewSpanLink.movementMethod = LinkMovementMethod.getInstance()
        val text = binding.txtViewSpanLink.text.toString()
        val spannable = SpanLinkBuilder()
            .setText(text)
            .setColorId(ResourcesCompat.getColor(resources, R.color.green, null))
            .setEndLinkIndex(text.length)
            .setStartLinkIndex(text.indexOf(getString(R.string.btn_register)))
            .setLinkCallback {
                findNavController().navigate(R.id.action_loginViewBindingFragment_to_registerViewBindingFragment)
            }.build()
        binding.txtViewSpanLink.text = spannable
    }

    private fun generateFormValidation() =
        LoginFormValidation(
            LoginFormElement(binding.txtFieldEmail, binding.txtFieldPassword),
            UserDataValidation()
        )

    private fun generateApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                when (it.response.isHaveCompleteProfile()) {
                    true -> findNavController().navigate(R.id.action_to_menuNavGraph)
                    else -> findNavController().navigate(R.id.action_loginViewBindingFragment_to_registerProfileViewBindingFragment)
                }
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

                binding.txtFieldPassword.text?.clear()
            },
        )

    private fun generateFormValidationCallback(): FormValidationCallback<LoginRequest> =
        FormValidationCallback(
            successCallback = {
                userViewModel.login(it)
            },
            errorCallback = {
                if (it.any()) binding.txtFieldPassword.text?.clear()

                it[R.id.txt_field_email]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutEmail, binding.txtFieldEmail),
                        getString(error.value)
                    )
                }

                it[R.id.txt_field_password]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutPassword, binding.txtFieldPassword),
                        getString(error.value)
                    )
                }
            }
        )
}