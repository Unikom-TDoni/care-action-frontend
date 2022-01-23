package edu.rpl.careaction.core.domain

data class ErrorResponse(
    val code: Int? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
)