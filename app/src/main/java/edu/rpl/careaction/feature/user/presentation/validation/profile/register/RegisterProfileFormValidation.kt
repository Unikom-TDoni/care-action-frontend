package edu.rpl.careaction.feature.user.presentation.validation.profile.register

import edu.rpl.careaction.R
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterProfileRequest
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult
import java.util.*

class RegisterProfileFormValidation(
    registerProfileFormElement: RegisterProfileFormElement,
    private val userDataValidation: UserDataValidation
) : FormValidation<RegisterProfileFormElement, FormValidationResult<RegisterProfileRequest>>(
    registerProfileFormElement
) {
    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<RegisterProfileRequest> {

        val dateOfBirth = element.dateOfBirthEditText.text.toString()
        val gender = element.genderAutoCompleteTextView.text.toString()
        val weight = element.widthEditText.text.toString().toFloatOrNull()
        val height = element.heightEditText.text.toString().toFloatOrNull()

        val elementValidationResult = mapOf(
            R.id.txt_field_weight to userDataValidation.validateWeight(weight),
            R.id.txt_field_height to userDataValidation.validateHeight(height),
            R.id.txt_field_date_birth to userDataValidation.validateDateOfBirth(
                if (dateOfBirth.isBlank()) null
                else DateUtility.convertStringToDate(dateOfBirth)
            ),
            R.id.auto_complete_text_view_gender to userDataValidation.validateGender(gender)
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
}