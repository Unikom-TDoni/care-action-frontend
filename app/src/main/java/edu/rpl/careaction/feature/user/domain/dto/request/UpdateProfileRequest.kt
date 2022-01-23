package edu.rpl.careaction.feature.user.domain.dto.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateProfileRequest(
    val name: RequestBody,
    val weight: RequestBody,
    val height: RequestBody,
    val gender: RequestBody,
    val birthdate: RequestBody,
    val picture: MultipartBody.Part?
)