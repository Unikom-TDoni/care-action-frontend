package edu.rpl.careaction.module.api

data class ErrorResponse(
    val code: Int? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
)