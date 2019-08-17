package org.mhacks.app.signin.ui.widget.validator

import org.mhacks.app.signin.R

class BlankValidator : Validator {
    override val errorRes: Int = R.string.blank_error

    override fun isValid(input: String) = input.isNotBlank()

}