package edu.rpl.careaction.feature.user.domain.dto.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("old_password") val currentPassword: String,
    @SerializedName("confirm_new_password") val confirmationPassword: String,
)