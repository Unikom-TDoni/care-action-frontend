package edu.rpl.careaction.module.validation

data class FormValidationCallback<in T>(
    val successCallback: (T) -> Unit,
    val errorCallback: (Map<Int, FormElementValidationResult.Error<Int>>) -> Unit
)
