package edu.rpl.careaction.feature.user.presentation.validation.profile

import edu.rpl.careaction.R
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult
import java.util.*
import kotlin.math.roundToInt

class RegisterProfileFormValidation(
    registerProfileFormElement: RegisterProfileFormElement,
) : FormValidation<RegisterProfileFormElement, FormValidationResult<RegisterProfileRequest>>(registerProfileFormElement) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<RegisterProfileRequest> {
        val dateOfBirth = element.dateOfBirthEditText.text.toString()
        val weight = element.widthEditText.text.toString().toFloatOrNull()
        val height = element.heightEditText.text.toString().toFloatOrNull()
        val gender = element.genderAutoCompleteTextView.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_weight to validateWeight(weight),
            R.id.txt_field_height to validateHeight(height),
            R.id.txt_field_date_birth to validateDateOfBirth(DateUtility.convertStringToDate(dateOfBirth)),
            R.id.auto_complete_text_view_gender to validateGender(gender)
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error<*> } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error<*> } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    RegisterProfileRequest(
                        (elementValidationResult[R.id.txt_field_weight] as FormElementValidationResult.Success<Int>).value,
                        (elementValidationResult[R.id.txt_field_height] as FormElementValidationResult.Success<Int>).value,
                        (elementValidationResult[R.id.auto_complete_text_view_gender] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_date_birth] as FormElementValidationResult.Success<Date>).value,
                    )
                )
        }
    }

    private fun validateGender(gender: String) = when {
        gender.isBlank() -> FormElementValidationResult.Error(R.string.gender_required_error_message)
        else -> FormElementValidationResult.Success(gender)
    }

    private fun validateDateOfBirth(dateOfBirth: Date?) = when {
        dateOfBirth == null -> FormElementValidationResult.Error(R.string.date_of_birth_required_error_message)
        else -> FormElementValidationResult.Success(dateOfBirth)
    }

    private fun validateWeight(weight: Float?) = when {
        weight == null -> FormElementValidationResult.Error(R.string.weight_required_error_message)
        weight < 0 -> FormElementValidationResult.Error(R.string.weight_must_positive_error_message)
        else -> FormElementValidationResult.Success(weight.roundToInt())
    }

    private fun validateHeight(height: Float?) = when {
        height == null -> FormElementValidationResult.Error(R.string.height_required_error_message)
        height < 0 -> FormElementValidationResult.Error(R.string.height_must_int_error_message)
        else -> FormElementValidationResult.Success(height.roundToInt())
    }
}