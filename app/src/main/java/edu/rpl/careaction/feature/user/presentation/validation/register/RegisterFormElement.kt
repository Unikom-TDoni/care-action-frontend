package edu.rpl.careaction.feature.user.presentation.validation.register

import android.widget.CheckBox
import android.widget.EditText

data class RegisterFormElement(
    val nameEditText: EditText,
    val emailEditText: EditText,
    val passwordEditText: EditText,
    val policyCheckBox: CheckBox
)