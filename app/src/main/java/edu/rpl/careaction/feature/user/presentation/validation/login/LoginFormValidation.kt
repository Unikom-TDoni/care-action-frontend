package edu.rpl.careaction.feature.user.presentation.validation.login

import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.domain.dto.request.LoginRequest
import edu.rpl.careaction.feature.user.presentation.validation.UserDataValidation
import edu.rpl.careaction.module.validation.FormElementValidationResult
import edu.rpl.careaction.module.validation.FormValidation
import edu.rpl.careaction.module.validation.FormValidationResult

class LoginFormValidation(
    formElement: LoginFormElement,
    private val userDataValidation: UserDataValidation
) : FormValidation<LoginFormElement, FormValidationResult<LoginRequest>>(formElement) {

    @Suppress("UNCHECKED_CAST")
    override fun validated(): FormValidationResult<LoginRequest> {
        val email = element.emailEditText.text.toString()
        val password = element.passwordEditText.text.toString()

        val elementValidationResult = mapOf(
            R.id.txt_field_email to userDataValidation.validateEmail(email),
            R.id.txt_field_password to userDataValidation.validatePassword(password)
        )

        return when {
            elementValidationResult.any { it.value is FormElementValidationResult.Error } ->
                FormValidationResult.Error(elementValidationResult.filterValues { it is FormElementValidationResult.Error } as Map<Int, FormElementValidationResult.Error<Int>>)
            else ->
                FormValidationResult.Success(
                    LoginRequest(
                        (elementValidationResult[R.id.txt_field_email] as FormElementValidationResult.Success<String>).value,
                        (elementValidationResult[R.id.txt_field_password] as FormElementValidationResult.Success<String>).value,
                    )
                )
        }
    }

}