package edu.rpl.careaction.feature.user.presentation.validation.profile

import android.widget.AutoCompleteTextView
import android.widget.EditText

data class ProfileFormElement(
    val widthEditText: EditText,
    val heightEditText: EditText,
    val dateOfBirthEditText: EditText,
    val genderAutoCompleteTextView: AutoCompleteTextView,
)