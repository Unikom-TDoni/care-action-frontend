package edu.rpl.careaction.feature.user.presentation.validation.register

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.domain.dto.request.RegisterRequest
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidationResult

class RegisterFormValidation(
    registerFormElement: RegisterFormElement,
    private val userDataValidation: UserDataValidation
) : FormValidation<RegisterFormElement, FormValidationResult<RegisterRequest>>(registerFormElement) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<RegisterRequest> {
        val policy = element.policyCheckBox.isChecked
        val name = element.nameEditText.text.toString()
        val email = element.emailEditText.text.toString()
        val password = element.passwordEditText.text.toString()

        val elementValidationResult = mapOf(
            R.id.check_box to validatePolicy(policy),
            R.id.txt_field_name to userDataValidation.validateName(name),
            R.id.txt_field_email to userDataValidation.validateEmail(email),
            R.id.txt_field_password to userDataValidation.validatePassword(password),
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

    private fun validatePolicy(privacy: Boolean) = when (privacy) {
        false -> FormElementValidationResult.Error(R.string.hello_world)
        else -> FormElementValidationResult.Success(privacy)
    }
}