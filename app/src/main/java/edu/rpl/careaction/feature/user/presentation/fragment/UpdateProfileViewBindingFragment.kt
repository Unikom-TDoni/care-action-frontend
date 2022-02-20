package edu.rpl.careaction.feature.user.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.core.utility.TextFieldUtility
import edu.rpl.careaction.databinding.FragmentUpdateProfileBinding
import edu.rpl.careaction.feature.user.domain.dto.request.UpdateProfileRequest
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.feature.user.presentation.validation.profile.update.UpdateProfileFormElement
import edu.rpl.careaction.feature.user.presentation.validation.profile.update.UpdateProfileFormValidation
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import edu.rpl.careaction.module.validation.FormValidationCallback
import edu.rpl.careaction.module.validation.FormValidationResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpdateProfileViewBindingFragment : ViewBindingFragment<FragmentUpdateProfileBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentUpdateProfileBinding::inflate
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)

    private var isAfterOpenCamera = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDropDownValue()
        initTextFieldEvent()
        initNavigationEvent()
        initSharedFlowEvent(generateApiCallback())
        initButtonEvent(generateFormValidation(), generateFormValidationCallback())
    }

    override fun onResume() {
        super.onResume()
        if (!isAfterOpenCamera) userViewModel.fetch()
        isAfterOpenCamera = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                isAfterOpenCamera = true
                binding.image.setImageURI(uri)
                binding.button.isEnabled = true
                binding.image.tag = uri.path.toString()
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initTextFieldEvent() {
        val datePickerDialog = DateUtility.generateDatePickerDialog(requireContext()) {
            binding.txtFieldDateBirth.setText(DateUtility.convertDateToString(it))
        }

        binding.txtFieldDateBirth.setOnClickListener {
            datePickerDialog.show()
        }

        TextFieldUtility.initOnTextFieldChangedErrorEvent(
            mapOf(
                binding.txtLayoutName to binding.txtFieldName,
                binding.txtLayoutHeight to binding.txtFieldHeight,
                binding.txtLayoutWeight to binding.txtFieldWeight,
                binding.txtLayoutDateBirth to binding.txtFieldDateBirth,
                binding.txtLayoutGender to binding.autoCompleteTextViewGender
            )
        )
    }

    private fun initButtonEvent(
        updateProfileFormValidation: UpdateProfileFormValidation,
        formValidationCallback: FormValidationCallback<UpdateProfileRequest>
    ) {
        binding.button.setOnClickListener {
            when (val validationResult = updateProfileFormValidation.validated()) {
                is FormValidationResult.Success -> formValidationCallback.successCallback(
                    validationResult.validatedData
                )
                is FormValidationResult.Error -> formValidationCallback.errorCallback(
                    validationResult.message
                )
            }
        }

        binding.btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(512, 512)
                .start()
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
    }

    private fun initSharedFlowEvent(apiCallback: ApiCallback<User, ErrorResponse>) =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.userSharedFlow.collect {
                    when (it) {
                        is ApiResult.Success -> apiCallback.successCallback(it)
                        is ApiResult.Loading -> apiCallback.loadingCallback(it)
                        is ApiResult.Error -> apiCallback.errorCallback(it)
                    }
                }
            }
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
        UpdateProfileFormValidation(
            UpdateProfileFormElement(
                binding.txtFieldName,
                binding.txtFieldWeight,
                binding.txtFieldHeight,
                binding.image,
                binding.txtFieldDateBirth,
                binding.autoCompleteTextViewGender,
            ), UserDataValidation()
        )

    private fun generateApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()

                binding.image.tag = null
                binding.txtFieldName.setText(it.response.name)
                binding.txtFieldHeight.setText(it.response.height.toString())
                binding.txtFieldWeight.setText(it.response.weight.toString())
                binding.autoCompleteTextViewGender.setText(it.response.gender, false)
                it.response.birthDate?.let { date ->
                    binding.txtFieldDateBirth.setText(DateUtility.convertDateToString(date))
                }

                Glide.with(requireContext())
                    .load(it.response.picture)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_profile_placeholder)
                    .into(binding.image)

                TextFieldUtility.initOnTextFieldChangedSameEvent(
                    listOf(
                        binding.txtFieldName,
                        binding.txtFieldHeight,
                        binding.txtFieldWeight,
                        binding.txtFieldDateBirth,
                        binding.autoCompleteTextViewGender
                    ),
                    Pair({
                        if (binding.button.isEnabled && binding.image.tag == null)
                            binding.button.isEnabled = false
                    }, {
                        if (!binding.button.isEnabled && binding.image.tag == null)
                            binding.button.isEnabled = true
                    })
                )
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

    private fun generateFormValidationCallback(): FormValidationCallback<UpdateProfileRequest> =
        FormValidationCallback(
            successCallback = {
                userViewModel.update(it)
            },
            errorCallback = {
                it[R.id.txt_field_name]?.let { error ->
                    binding.txtLayoutName.error = getString(error.value)
                }

                it[R.id.txt_field_weight]?.let { error ->
                    binding.txtLayoutWeight.error = getString(error.value)
                }

                it[R.id.txt_field_height]?.let { error ->
                    binding.txtLayoutHeight.error = getString(error.value)
                }

                it[R.id.txt_field_date_birth]?.let { error ->
                    binding.txtLayoutDateBirth.error = getString(error.value)
                }

                it[R.id.auto_complete_text_view_gender]?.let { error ->
                    binding.txtLayoutGender.error = getString(error.value)
                }
            }
        )
}