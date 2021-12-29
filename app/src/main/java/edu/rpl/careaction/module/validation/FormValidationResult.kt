package edu.rpl.careaction.module.validation

sealed class FormValidationResult<out T> {
    data class Success<out T>(val validatedData: T) : FormValidationResult<T>()
    data class Error<out T>(val message: Map<Int, FormElementValidationResult.Error<Int>>) : FormValidationResult<T>()
}