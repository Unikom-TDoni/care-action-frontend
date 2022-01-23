package edu.rpl.careaction.feature.user.presentation.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.builder.SpanLinkBuilder
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.core.utility.TextFieldUtility
import edu.rpl.careaction.databinding.FragmentRegisterBinding
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.feature.user.presentation.validation.register.RegisterFormElement
import edu.rpl.careaction.feature.user.presentation.validation.register.RegisterFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterViewBindingFragment : ViewBindingFragment<FragmentRegisterBinding>() {

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.welcome_nav_graph)
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentRegisterBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextViewLink()
        initCheckboxEvent()
        initTextFieldEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateValidationCallback())

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
        registerFormValidation: RegisterFormValidation,
        validationCallback: FormValidationCallback<RegisterRequest>
    ) =
        binding.btnRegister.setOnClickListener {
            when (val validationResult = registerFormValidation.validated()) {
                is FormValidationResult.Success -> validationCallback.successCallback(
                    validationResult.validatedData
                )
                is FormValidationResult.Error -> validationCallback.errorCallback(validationResult.message)
            }
        }

    private fun initTextFieldEvent() {
        TextFieldUtility.initOnTextFieldChangedCustomErrorEvent(
            mapOf(
                binding.txtLayoutName to binding.txtFieldName,
                binding.txtLayoutEmail to binding.txtFieldEmail,
                binding.txtLayoutPassword to binding.txtFieldPassword,
            )
        )
    }

    private fun initCheckboxEvent() {
        val defaultColorStateList = binding.checkBox.buttonTintList
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) return@setOnCheckedChangeListener
            if (binding.checkBox.buttonTintList != defaultColorStateList)
                binding.checkBox.buttonTintList = defaultColorStateList
        }
    }

    private fun initTextViewLink() {
        binding.txtViewSpanLink.movementMethod = LinkMovementMethod.getInstance()
        val text = binding.txtViewSpanLink.text.toString()
        val spannable = SpanLinkBuilder()
            .setText(text)
            .setEndLinkIndex(text.length)
            .setStartLinkIndex(text.indexOf(getString(R.string.btn_login)))
            .setColorId(ResourcesCompat.getColor(resources, R.color.green, null))
            .setLinkCallback {
                findNavController().navigate(R.id.action_registerViewBindingFragment_to_loginViewBindingFragment)
            }.build()
        binding.txtViewSpanLink.text = spannable
    }

    private fun generateFormValidation() =
        RegisterFormValidation(
            RegisterFormElement(
                binding.txtFieldName,
                binding.txtFieldEmail,
                binding.txtFieldPassword,
                binding.checkBox
            ), UserDataValidation()
        )

    private fun generateApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                findNavController().navigate(R.id.action_registerViewBindingFragment_to_registerProfileViewBindingFragment)
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
            }
        )

    private fun generateValidationCallback(): FormValidationCallback<RegisterRequest> =
        FormValidationCallback(
            successCallback = {
                userViewModel.register(it)
            },
            errorCallback = {
                if (it.any()) binding.txtFieldPassword.text?.clear()

                it[R.id.txt_field_name]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutName, binding.txtFieldName),
                        getString(error.value)
                    )
                }

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

                it[R.id.check_box]?.let {
                    binding.checkBox.buttonTintList = ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.red,
                            null
                        )
                    )
                }
            }
        )
}