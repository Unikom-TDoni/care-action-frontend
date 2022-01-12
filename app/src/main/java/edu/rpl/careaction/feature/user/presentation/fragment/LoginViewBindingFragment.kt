package edu.rpl.careaction.feature.user.presentation.fragment


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.ApplicationNavGraphDirections
import edu.rpl.careaction.R
import edu.rpl.careaction.core.builder.SpanLinkBuilder
import edu.rpl.careaction.feature.support.loading.LoadingDialogFragment
import edu.rpl.careaction.core.utility.TextFieldErrorUtility
import edu.rpl.careaction.databinding.FragmentLoginBinding
import edu.rpl.careaction.feature.support.error.ErrorParcelable
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserEmailPasswordValidation
import edu.rpl.careaction.feature.user.presentation.validation.login.LoginFormElement
import edu.rpl.careaction.feature.user.presentation.validation.login.LoginFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewBindingFragment : ViewBindingFragment<FragmentLoginBinding>() {

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.welcome_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextViewLink()
        initTextFieldEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateValidationCallback())

        binding.txtFieldEmail.setText("test@gmail.com")
        binding.txtFieldPassword.setText("12345678")
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

    private fun initTextFieldEvent() {
        TextFieldErrorUtility.initOnTextFieldChangedErrorEvent(
            mapOf(
                binding.txtLayoutEmail to binding.txtFieldEmail,
                binding.txtLayoutPassword to binding.txtFieldPassword,
            )
        )
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
            UserEmailPasswordValidation()
        )

    private fun generateApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = fun(success) {
                LoadingDialogFragment.dismiss()
                when (success.response.isHaveCompleteProfile()) {
                    true -> findNavController().navigate(R.id.action_to_mainMenuViewBindingFragment)
                    else -> findNavController().navigate(R.id.action_loginViewBindingFragment_to_registerProfileViewBindingFragment)
                }
            },
            loadingCallback = fun(_) {
                LoadingDialogFragment.show(
                    parentFragmentManager,
                    String()
                )
            },
            errorCallback = fun(error) {
                LoadingDialogFragment.dismiss()
                error.response.throwable?.let {
                    findNavController().navigate(
                        ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                            ErrorParcelable(it)
                        )
                    )
                } ?: Toast.makeText(context, error.response.message, Toast.LENGTH_SHORT).show()
            },
        )

    private fun generateValidationCallback(): FormValidationCallback<LoginRequest> =
        FormValidationCallback(
            successCallback = fun(validatedData) {
                userViewModel.login(validatedData)
            },
            errorCallback = fun(errorMessage) {
                errorMessage[R.id.txt_field_email]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutEmail, binding.txtFieldEmail),
                        getString(it.value)
                    )
                }

                errorMessage[R.id.txt_field_password]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutPassword, binding.txtFieldPassword),
                        getString(it.value)
                    )
                }
            }
        )
}