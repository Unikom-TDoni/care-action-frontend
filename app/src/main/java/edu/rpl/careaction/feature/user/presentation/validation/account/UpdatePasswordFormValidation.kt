package edu.rpl.careaction.feature.user.presentation.validation.account

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.domain.dto.request.UpdatePasswordRequest
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult

class UpdatePasswordFormValidation(
    updatePasswordFormElement: UpdatePasswordFormElement,
) : FormValidation<UpdatePasswordFormElement, FormValidationResult<UpdatePasswordRequest>>(
    updatePasswordFormElement
) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<UpdatePasswordRequest> {
        val newPassword = element.newPassword.text.toString()
        val currentPassword = element.currentPassword.text.toString()
        val confirmationPassword = element.confirmationPassword.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_password to validateCurrentPassword(currentPassword),
            R.id.txt_field_new_password to validateNewPassword(newPassword, currentPassword),
            R.id.txt_field_confirmation_password to validateConfirmationPassword(
                confirmationPassword,
                newPassword
            ),
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    UpdatePasswordRequest(
                        (elementValidationResult[R.id.txt_field_new_password] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_password] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_confirmation_password] as FormElementValidationResult.Success<String>).value,
                    )
                )
        }
    }

    private fun validateCurrentPassword(currentPassword: String) = when {
        currentPassword.isBlank() -> FormElementValidationResult.Error(R.string.current_password_required_error_message)
        currentPassword.length < 8 -> FormElementValidationResult.Error(R.string.password_length_below_minimum_error_message)
        else -> FormElementValidationResult.Success(currentPassword)
    }

    private fun validateNewPassword(newPassword: String, currentPassword: String) = when {
        newPassword.isBlank() -> FormElementValidationResult.Error(R.string.new_password_required_error_message)
        newPassword.length < 8 -> FormElementValidationResult.Error(R.string.password_length_below_minimum_error_message)
        newPassword == currentPassword -> FormElementValidationResult.Error(R.string.new_password_same_error_message)
        else -> FormElementValidationResult.Success(newPassword)
    }

    private fun validateConfirmationPassword(confirmationPassword: String, newPassword: String) =
        when {
            confirmationPassword != newPassword -> FormElementValidationResult.Error(R.string.not_same_confirmation_password_error_message)
            else -> FormElementValidationResult.Success(confirmationPassword)
        }
}