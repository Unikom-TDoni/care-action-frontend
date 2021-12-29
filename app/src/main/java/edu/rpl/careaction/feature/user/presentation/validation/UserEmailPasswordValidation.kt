package edu.rpl.careaction.feature.user.presentation.validation

import android.util.Patterns
import edu.rpl.careaction.R
import edu.rpl.careaction.module.validation.FormElementValidationResult

class UserEmailPasswordValidation {
    fun validateEmail(email: String) = when {
        email.isBlank() -> FormElementValidationResult.Error(R.string.email_required_error_message)
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> FormElementValidationResult.Error(R.string.email_format_not_valid_error_message)
        else -> FormElementValidationResult.Success(email)
    }

    fun validatePassword(password: String) = when {
        password.isBlank() -> FormElementValidationResult.Error(R.string.password_required_error_message)
        password.length < 8 -> FormElementValidationResult.Error(R.string.password_length_below_minimum_error_message)
        else -> FormElementValidationResult.Success(password)
    }
}