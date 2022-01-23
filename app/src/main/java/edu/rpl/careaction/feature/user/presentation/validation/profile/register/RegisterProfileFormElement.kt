package edu.rpl.careaction.feature.user.presentation.validation.profile.register

import android.widget.EditText
import android.widget.AutoCompleteTextView

data class RegisterProfileFormElement(
    val widthEditText: EditText,
    val heightEditText: EditText,
    val dateOfBirthEditText: EditText,
    val genderAutoCompleteTextView: AutoCompleteTextView,
)