package edu.rpl.careaction.feature.user.presentation.validation.account

import android.widget.EditText

data class UpdatePasswordFormElement(
    val newPassword: EditText,
    val currentPassword: EditText,
    val confirmationPassword: EditText
)