package edu.rpl.careaction.feature.user.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.presentation.ApplicationViewModel
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.core.utility.TextFieldUtility
import edu.rpl.careaction.databinding.FragmentRegisterProfileBinding
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.feature.user.presentation.validation.profile.register.RegisterProfileFormElement
import edu.rpl.careaction.feature.user.presentation.validation.profile.register.RegisterProfileFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class RegisterProfileViewBindingFragment :
    ViewBindingFragment<FragmentRegisterProfileBinding>() {

    private val applicationViewModel: ApplicationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.landing_nav_graph)

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentRegisterProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDropDownValue()
        initTextFieldEvent()
        initNavigationEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateFormValidationCallback())
        applicationViewModel.hideSplashScreenWhenActive()
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<ResponseBody, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.defaultUserShardFlow.collect {
                    when (it) {
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                    }
                }
            }
        }

    private fun initButtonEvent(
        registerProfileFormValidation: RegisterProfileFormValidation,
        validationCallback: FormValidationCallback<RegisterProfileRequest>
    ) {
        binding.btnMainMenu.setOnClickListener {
            when (val validationResult = registerProfileFormValidation.validated()) {
                is FormValidationResult.Success -> validationCallback.successCallback(
                    validationResult.validatedData
                )
                is FormValidationResult.Error -> validationCallback.errorCallback(validationResult.message)
            }
        }
    }

    private fun initTextFieldEvent() {
        TextFieldUtility.initOnTextFieldChangedCustomErrorEvent(
            mapOf(
                binding.txtLayoutHeight to binding.txtFieldHeight,
                binding.txtLayoutWeight to binding.txtFieldWeight,
                binding.txtLayoutDateBirth to binding.txtFieldDateBirth,
                binding.txtLayoutGender to binding.autoCompleteTextViewGender
            )
        )

        val datePickerDialog = DateUtility.generateDatePickerDialog(requireContext()) {
            binding.txtFieldDateBirth.setText(DateUtility.convertDateToString(it))
        }
        binding.txtFieldDateBirth.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun initNavigationEvent() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                var pressedTime = 0L
                override fun handleOnBackPressed() {
                    if (pressedTime + 2000 > System.currentTimeMillis()) requireActivity().finish()
                    else Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_back_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                    pressedTime = System.currentTimeMillis()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initDropDownValue() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            resources.getStringArray(R.array.gender_items)
        )
        binding.autoCompleteTextViewGender.setAdapter(adapter)
    }

    private fun generateFormValidation() =
        RegisterProfileFormValidation(
            RegisterProfileFormElement(
                binding.txtFieldWeight,
                binding.txtFieldHeight,
                binding.txtFieldDateBirth,
                binding.autoCompleteTextViewGender,
            ), UserDataValidation()
        )

    private fun generateApiCallback(): ApiCallback<ResponseBody, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                findNavController().navigate(R.id.action_to_menuNavGraph)
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
            }
        )

    private fun generateFormValidationCallback(): FormValidationCallback<RegisterProfileRequest> =
        FormValidationCallback(
            successCallback = {
                userViewModel.registerProfile(it)
            },
            errorCallback = {
                it[R.id.txt_field_weight]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutWeight, binding.txtFieldWeight),
                        getString(error.value)
                    )
                }

                it[R.id.txt_field_height]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutHeight, binding.txtFieldHeight),
                        getString(error.value)
                    )
                }

                it[R.id.txt_field_date_birth]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutDateBirth, binding.txtFieldDateBirth),
                        getString(error.value)
                    )
                }

                it[R.id.auto_complete_text_view_gender]?.let { error ->
                    TextFieldUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutGender, binding.autoCompleteTextViewGender),
                        getString(error.value)
                    )
                }
            }
        )
}