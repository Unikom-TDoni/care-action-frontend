package edu.rpl.careaction.core.utility

import edu.rpl.careaction.R
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout

class TextFieldUtility {
    companion object {
        fun initOnTextFieldChangedErrorEvent(
            textFieldElements: Map<TextInputLayout, EditText>,
            callback: (TextInputLayout, EditText) -> Unit = { _, _ -> }
        ) =
            textFieldElements.forEach { (key, value) ->
                value.doAfterTextChanged {
                    if (key.isErrorEnabled) {
                        key.isErrorEnabled = false
                        callback(key, value)
                    }
                }
            }

        fun initOnTextFieldChangedCustomErrorEvent(textFieldElements: Map<TextInputLayout, EditText>) =
            initOnTextFieldChangedErrorEvent(
                textFieldElements
            ) { _, editText -> editText.setBackgroundResource(R.drawable.bg_txt_field_state) }

        fun initOnTextFieldChangedSameEvent(
            textFieldElements: Collection<EditText>,
            callback: Pair<() -> Unit, () -> Unit>
        ) {
            val defaultText = textFieldElements.map { it.text.toString() }
            textFieldElements.forEachIndexed { index, editText ->
                editText.doAfterTextChanged {
                    if (editText.text.toString() == defaultText[index]) callback.first()
                    else callback.second()
                }
            }
        }
        
        fun setTextFieldErrorMessage(
            textFieldElements: Pair<TextInputLayout, EditText>,
            errorMessage: String
        ) {
            textFieldElements.first.error = errorMessage
            textFieldElements.second.setBackgroundResource(R.drawable.bg_txt_field_state_error)
        }
    }
}