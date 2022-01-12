package edu.rpl.careaction.feature.user.presentation.fragment


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.ApplicationNavGraphDirections
import edu.rpl.careaction.R
import edu.rpl.careaction.core.presentation.ApplicationViewModel
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.core.utility.TextFieldErrorUtility
import edu.rpl.careaction.databinding.FragmentRegisterProfileBinding
import edu.rpl.careaction.feature.support.error.ErrorParcelable
import edu.rpl.careaction.feature.support.loading.LoadingDialogFragment
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.profile.RegisterProfileFormElement
import edu.rpl.careaction.feature.user.presentation.validation.profile.RegisterProfileFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.*

class RegisterProfileViewBindingFragment :
    ViewBindingFragment<FragmentRegisterProfileBinding>() {

    private val applicationViewModel: ApplicationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.welcome_nav_graph)

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentRegisterProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDropDownValue()
        initTextFieldEvent()
        initSharedFlowEvent(generateApiCallback())
        initTextFieldEvent(generateDatePickerDialog())
        initButtonEvent(generateFormValidation(), generateValidationCallback())
        applicationViewModel.hideSplashScreenWhenActive()
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<ResponseBody, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.defaultUserUpdateSharedFlow.collect {
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
        TextFieldErrorUtility.initOnTextFieldChangedErrorEvent(
            mapOf(
                binding.txtLayoutHeight to binding.txtFieldHeight,
                binding.txtLayoutWeight to binding.txtFieldWeight,
                binding.txtLayoutDateBirth to binding.txtFieldDateBirth,
                binding.txtLayoutGender to binding.autoCompleteTextViewGender
            )
        )
    }

    private fun initDropDownValue() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            resources.getStringArray(R.array.gender_items)
        )
        binding.autoCompleteTextViewGender.setAdapter(adapter)
    }

    private fun initTextFieldEvent(datePickerDialog: DatePickerDialog) {
        binding.txtFieldDateBirth.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun generateFormValidation() =
        RegisterProfileFormValidation(
            RegisterProfileFormElement(
                binding.txtFieldWeight,
                binding.txtFieldHeight,
                binding.txtFieldDateBirth,
                binding.autoCompleteTextViewGender,
            )
        )

    @SuppressLint("SetTextI18n")
    private fun generateDatePickerDialog(): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val dialogResult = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding.txtFieldDateBirth.setText(DateUtility.convertDateToString(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialogResult.datePicker.maxDate = Date().time
        return dialogResult
    }

    private fun generateApiCallback(): ApiCallback<ResponseBody, ErrorResponse> =
        ApiCallback(
            successCallback = fun(_) {
                LoadingDialogFragment.dismiss()
                findNavController().navigate(R.id.action_to_mainMenuViewBindingFragment)
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
            }
        )

    private fun generateValidationCallback(): FormValidationCallback<RegisterProfileRequest> =
        FormValidationCallback(
            successCallback = fun(validatedData) {
                userViewModel.update(validatedData)
            },
            errorCallback = fun(errorMessage) {
                errorMessage[R.id.txt_field_weight]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutWeight, binding.txtFieldWeight),
                        getString(it.value)
                    )
                }

                errorMessage[R.id.txt_field_height]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutHeight, binding.txtFieldHeight),
                        getString(it.value)
                    )
                }

                errorMessage[R.id.txt_field_date_birth]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutDateBirth, binding.txtFieldDateBirth),
                        getString(it.value)
                    )
                }

                errorMessage[R.id.auto_complete_text_view_gender]?.let {
                    TextFieldErrorUtility.setTextFieldErrorMessage(
                        Pair(binding.txtLayoutGender, binding.autoCompleteTextViewGender),
                        getString(it.value)
                    )
                }
            }
        )
}