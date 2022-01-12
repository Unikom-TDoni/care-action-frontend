package edu.rpl.careaction.feature.user.presentation.validation.register

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.feature.user.presentation.validation.UserEmailPasswordValidation
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidationResult

class RegisterFormValidation(
    registerFormElement: RegisterFormElement,
    private val userEmailPasswordValidation: UserEmailPasswordValidation
) : FormValidation<RegisterFormElement, FormValidationResult<RegisterRequest>>(registerFormElement) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<RegisterRequest> {
        val policy = element.policyCheckBox.isChecked
        val name = element.nameEditText.text.toString()
        val email = element.emailEditText.text.toString()
        val password = element.passwordEditText.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_name to validateName(name),
            R.id.check_box to validatePolicy(policy),
            R.id.txt_field_email to userEmailPasswordValidation.validateEmail(email),
            R.id.txt_field_password to userEmailPasswordValidation.validatePassword(password),
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    RegisterRequest(
                        (elementValidationResult[R.id.txt_field_name] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_email] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_password] as FormElementValidationResult.Success<String>).value,
                    )
                )
        }
    }

    private fun validateName(name: String) = when {
        name.isBlank() -> FormElementValidationResult.Error(R.string.name_required_error_message)
        name.length < 3 -> FormElementValidationResult.Error(R.string.name_below_minimum_error_message)
        else -> FormElementValidationResult.Success(name)
    }

    private fun validatePolicy(privacy: Boolean) = when(privacy) {
        false -> FormElementValidationResult.Error(R.string.policy_required_error_message)
        else -> FormElementValidationResult.Success(privacy)
    }
}