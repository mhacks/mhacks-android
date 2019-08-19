package org.mhacks.app.signin.ui.widget.validator

import android.content.Context
import android.service.autofill.Validators
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.R
import com.google.android.material.textfield.TextInputLayout

private val DEF_STYLE_RES = R.style.Widget_MaterialComponents_TextInputLayout_FilledBox

interface ValidatorView {

    var validators: List<Validator>

    fun validate(): Boolean

}

class ValidatorTextInputLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : TextInputLayout(ContextThemeWrapper(context, DEF_STYLE_RES), attrs), ValidatorView {

    override var validators: List<Validator> = listOf()

    /**
     * Invoke this when you want to validate the contained `EditText` input text against the
     * provided [Validator]. For validating multiple `ValidatorTextInputLayout`
     * objects at once, call [Validators.validate]. Throws an
     * `IllegalStateException` if either no validator has been set or an error is triggered
     * and no error label is set.
     */
    override fun validate(): Boolean {
        var input: CharSequence = ""
        val editText = editText
        if (editText != null) {
            input = editText.text
        }

        validators.forEach { validator ->
            val valid = validator.isValid(input.toString())
            val errorLabel = validator.errorRes

            if (valid) {
                error = null
            } else {
                error = context.getString(errorLabel)
            }
        }
        return error == null
    }

}

fun List<ValidatorView>.validate(): Boolean {
    var valid = true
    for (validatorView in this) {
        if (!validatorView.validate()) {
            valid = false
        }
    }
    return valid
}