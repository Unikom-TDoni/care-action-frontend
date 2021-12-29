package edu.rpl.careaction.feature.user.domain.dto.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)