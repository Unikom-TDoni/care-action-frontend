package edu.rpl.careaction.core.utility

import edu.rpl.careaction.R
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

class TextFieldErrorUtility {

    companion object {
        fun initOnTextFieldChangedErrorEvent(textFieldElements: Map<TextInputLayout, EditText>) =
            textFieldElements.forEach { (key, value) ->
                value.doOnTextChanged { _, _, _, _ ->
                    if (key.isErrorEnabled) {
                        key.isErrorEnabled = false
                        value.setBackgroundResource(R.drawable.bg_txt_field_state)
                    }
                }
            }

        fun setTextFieldErrorMessage(textFieldElements: Pair<TextInputLayout, EditText>, errorMessage: String) {
            textFieldElements.first.error = errorMessage
            textFieldElements.second.setBackgroundResource(R.drawable.bg_txt_field_state_error)
        }
    }

}