package edu.rpl.careaction.feature.user.presentation.validation.profile

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.domain.dto.request.ProfileRequest
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult

class ProfileFormValidation(
    profileFormElement: ProfileFormElement,
) : FormValidation<ProfileFormElement, FormValidationResult<ProfileRequest>>(profileFormElement) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<ProfileRequest> {
        val dateOfBirth = element.dateOfBirthEditText.text.toString()
        val weight = element.widthEditText.text.toString().toIntOrNull()
        val height = element.heightEditText.text.toString().toIntOrNull()
        val gender = element.genderAutoCompleteTextView.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_weight to validateWeight(weight),
            R.id.txt_field_height to validateHeight(height),
            R.id.txt_field_date_birth to validateDateOfBirth(dateOfBirth),
            R.id.auto_complete_text_view_gender to validateGender(gender)
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error<*> } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error<*> } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    ProfileRequest(
                        (elementValidationResult[R.id.auto_complete_text_view_gender] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_date_birth] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_weight] as FormElementValidationResult.Success<Int>).value,
                        (elementValidationResult[R.id.txt_field_height] as FormElementValidationResult.Success<Int>).value
                    )
                )
        }
    }

    private fun validateGender(gender: String) = when {
        gender.isBlank() -> FormElementValidationResult.Error(R.string.gender_required_error_message)
        else -> FormElementValidationResult.Success(gender)
    }

    private fun validateDateOfBirth(dateOfBirth: String) = when {
        dateOfBirth.isBlank() -> FormElementValidationResult.Error(R.string.date_of_birth_required_error_message)
        else -> FormElementValidationResult.Success(dateOfBirth)
    }

    private fun validateWeight(weight: Int?) = when (weight) {
        null -> FormElementValidationResult.Error(R.string.weight_required_error_message)
        else -> FormElementValidationResult.Success(weight)
    }

    private fun validateHeight(height: Int?) = when (height) {
        null -> FormElementValidationResult.Error(R.string.height_required_error_message)
        else -> FormElementValidationResult.Success(height)
    }
}