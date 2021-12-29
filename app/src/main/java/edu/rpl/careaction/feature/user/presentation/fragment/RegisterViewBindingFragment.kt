package edu.rpl.careaction.feature.user.presentation.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import edu.rpl.careaction.R
import edu.rpl.careaction.core.presentation.dialog.LoadingDialogFragment
import edu.rpl.careaction.core.utility.TextFieldErrorUtility
import edu.rpl.careaction.databinding.FragmentRegisterBinding
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserEmailPasswordValidation
import edu.rpl.careaction.feature.user.presentation.validation.register.RegisterFormElement
import edu.rpl.careaction.feature.user.presentation.validation.register.RegisterFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterViewBindingFragment : ViewBindingFragment<FragmentRegisterBinding>() {

    private val userViewModel: UserViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentRegisterBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextViewLink()
        initTextFieldEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateValidationCallback())

        binding.txtFieldName.setText("sdf23234234")
        binding.txtFieldEmail.setText("budi@gmail.com")
        binding.txtFieldPassword.setText("sdfsdfsdfsfsdf")
        binding.checkBoxPolicy.isChecked = true
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<User, String>) =
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userRegisterSharedFlow.collect {
                when (it) {
                    is ApiResult.Error -> apiCallback.errorCallback(it)
                    is ApiResult.Loading -> apiCallback.loadingCallback(it)
                    is ApiResult.Success -> apiCallback.successCallback(it)
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
        TextFieldErrorUtility.initOnTextFieldChangedErrorEvent(
            mapOf(
                binding.txtLayoutName to binding.txtFieldName,
                binding.txtLayoutEmail to binding.txtFieldEmail,
                binding.txtLayoutPassword to binding.txtFieldPassword,
            )
        )
    }

    private fun initTextViewLink() {
        binding.txtViewLogin.movementMethod = LinkMovementMethod.getInstance()
        val spannable = SpannableString(getString(R.string.txt_login_link))
        val startTextSpanIndex = spannable.indexOf(getString(R.string.btn_login))
        val endTextSpanIndex = spannable.length
        val spanFlags = Spannable.SPAN_EXCLUSIVE_INCLUSIVE

        spannable.setSpan(UnderlineSpan(), startTextSpanIndex, endTextSpanIndex, spanFlags)
        spannable.setSpan(
            ForegroundColorSpan(
                ResourcesCompat.getColor(
                    resources,
                    R.color.green,
                    null
                )
            ), startTextSpanIndex, endTextSpanIndex, spanFlags
        )
        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(view: View) {
                    findNavController().navigate(R.id.action_registerViewBindingFragment_to_loginViewBindingFragment)
                }
            }, startTextSpanIndex, endTextSpanIndex, spanFlags
        )
        binding.txtViewLogin.text = spannable
    }

    private fun generateFormValidation() =
        RegisterFormValidation(
            RegisterFormElement(
                binding.txtFieldName,
                binding.txtFieldEmail,
                binding.txtFieldPassword,
                binding.checkBoxPolicy
            ), UserEmailPasswordValidation()
        )

    private fun generateApiCallback(): ApiCallback<User, String> =
        ApiCallback(
            successCallback = fun(_) {
                LoadingDialogFragment.dismiss()
                findNavController().navigate(R.id.action_registerViewBindingFragment_to_completeProfileFormViewBindingFragment)
            },
            loadingCallback = fun(_) {
                LoadingDialogFragment.show(parentFragmentManager, String())
            },
            errorCallback = fun(error) {
                LoadingDialogFragment.dismiss()
                Toast.makeText(context, error.response, Toast.LENGTH_SHORT).show()
            }
        )

    private fun generateValidationCallback(): FormValidationCallback<RegisterRequest> =
        FormValidationCallback(
            successCallback = fun(validatedData) {
                userViewModel.register(validatedData)
            },
            errorCallback = fun(errorMessage) {
                errorMessage[R.id.txt_field_name]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutName, binding.txtFieldName),
                        getString(it.value)
                    )
                }

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

                errorMessage[R.id.check_box_policy]?.let {

                }
            }
        )
}