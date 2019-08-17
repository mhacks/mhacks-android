package org.mhacks.app.signin.ui.widget.validator

import android.util.Patterns
import org.mhacks.app.signin.R

class EmailValidator : Validator {
    override val errorRes: Int = R.string.email_error

    override fun isValid(input: String) =
            Patterns.EMAIL_ADDRESS.matcher(input).matches()

}