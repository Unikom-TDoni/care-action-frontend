package edu.rpl.careaction.feature.user.presentation.validation

import android.util.Patterns
import edu.rpl.careaction.R
import edu.rpl.careaction.module.validation.FormElementValidationResult
import java.util.*
import kotlin.math.roundToInt

class UserDataValidation {
    fun validateEmail(email: String) = when {
        email.isBlank() -> FormElementValidationResult.Error(R.string.email_required_error_message)
        !Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() -> FormElementValidationResult.Error(R.string.email_format_not_valid_error_message)
        else -> FormElementValidationResult.Success(email)
    }

    fun validatePassword(password: String) = when {
        password.isBlank() -> FormElementValidationResult.Error(R.string.password_required_error_message)
        password.length < 8 -> FormElementValidationResult.Error(R.string.password_length_below_minimum_error_message)
        else -> FormElementValidationResult.Success(password)
    }

    fun validateGender(gender: String) = when {
        gender.isBlank() -> FormElementValidationResult.Error(R.string.gender_required_error_message)
        else -> FormElementValidationResult.Success(gender)
    }

    fun validateDateOfBirth(dateOfBirth: Date?) = when (dateOfBirth) {
        null -> FormElementValidationResult.Error(R.string.date_of_birth_required_error_message)
        else -> FormElementValidationResult.Success(dateOfBirth)
    }

    fun validateWeight(weight: Float?) = when {
        weight == null -> FormElementValidationResult.Error(R.string.weight_required_error_message)
        weight < 0 || weight > 400 -> FormElementValidationResult.Error(R.string.weight_must_valid_error_message)
        else -> FormElementValidationResult.Success(weight.roundToInt())
    }

    fun validateHeight(height: Float?) = when {
        height == null -> FormElementValidationResult.Error(R.string.height_required_error_message)
        height < 0 || height > 300 -> FormElementValidationResult.Error(R.string.height_must_valid_error_message)
        else -> FormElementValidationResult.Success(height.roundToInt())
    }

    fun validateName(name: String) = when {
        name.isBlank() -> FormElementValidationResult.Error(R.string.name_required_error_message)
        name.length < 3 -> FormElementValidationResult.Error(R.string.name_below_minimum_error_message)
        else -> FormElementValidationResult.Success(name)
    }
}