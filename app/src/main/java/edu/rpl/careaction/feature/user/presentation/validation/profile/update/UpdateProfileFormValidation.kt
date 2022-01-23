package edu.rpl.careaction.feature.user.presentation.validation.profile.update

import edu.rpl.careaction.R
import edu.rpl.careaction.core.utility.DateUtility
import edu.rpl.careaction.feature.user.domain.dto.request.UpdateProfileRequest
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class UpdateProfileFormValidation(
    updateProfileFormElement: UpdateProfileFormElement,
    private val userDataValidation: UserDataValidation
) : FormValidation<UpdateProfileFormElement, FormValidationResult<UpdateProfileRequest>>(
    updateProfileFormElement
) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<UpdateProfileRequest> {
        val name = element.nameEditText.text.toString()
        val weight = element.widthEditText.text.toString().toFloatOrNull()
        val height = element.heightEditText.text.toString().toFloatOrNull()
        val gender = element.genderAutoCompleteTextView.text.toString()
        val dateOfBirth = element.dateOfBirthEditText.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_name to userDataValidation.validateName(name),
            R.id.txt_field_weight to userDataValidation.validateWeight(weight),
            R.id.txt_field_height to userDataValidation.validateHeight(height),
            R.id.auto_complete_text_view_gender to userDataValidation.validateGender(gender),
            R.id.txt_field_date_birth to userDataValidation.validateDateOfBirth(
                if (dateOfBirth.isBlank()) null
                else DateUtility.convertStringToDate(dateOfBirth)
            ),
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error<*> } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error<*> } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    UpdateProfileRequest(
                        RequestBody.create(
                            MultipartBody.FORM,
                            (elementValidationResult[R.id.txt_field_name] as FormElementValidationResult.Success<String>).value
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            (elementValidationResult[R.id.txt_field_weight] as FormElementValidationResult.Success<Int>).value.toString()
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            (elementValidationResult[R.id.txt_field_height] as FormElementValidationResult.Success<Int>).value.toString()
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            (elementValidationResult[R.id.auto_complete_text_view_gender] as FormElementValidationResult.Success<String>).value
                        ),
                        RequestBody.create(
                            MultipartBody.FORM,
                            (elementValidationResult[R.id.txt_field_date_birth] as FormElementValidationResult.Success<Date>).value.toString()
                        ),
                        if (element.photoImageView.tag != null) {
                            val file = File(element.photoImageView.tag.toString())
                            MultipartBody.Part.createFormData(
                                "picture",
                                file.name,
                                RequestBody.create(
                                    MultipartBody.FORM,
                                    file
                                )
                            )
                        } else null
                    )
                )
        }
    }
}