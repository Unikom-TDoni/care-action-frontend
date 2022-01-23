package edu.rpl.careaction.feature.user.presentation.validation.profile.update

import android.widget.ImageView
import android.widget.EditText
import android.widget.AutoCompleteTextView

data class UpdateProfileFormElement(
    val nameEditText: EditText,
    val widthEditText: EditText,
    val heightEditText: EditText,
    val photoImageView: ImageView,
    val dateOfBirthEditText: EditText,
    val genderAutoCompleteTextView: AutoCompleteTextView,
)