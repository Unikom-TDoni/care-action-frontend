package edu.rpl.careaction.feature.user.presentation.validation.profile

import android.widget.AutoCompleteTextView
import android.widget.EditText

data class RegisterProfileFormElement(
    val widthEditText: EditText,
    val heightEditText: EditText,
    val dateOfBirthEditText: EditText,
    val genderAutoCompleteTextView: AutoCompleteTextView,
)