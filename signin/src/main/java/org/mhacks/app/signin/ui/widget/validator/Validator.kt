package org.mhacks.app.signin.ui.widget.validator

import androidx.annotation.StringRes

/** Validates input for [ValidatorTextInputLayout] to meet some requirement.  */
interface Validator {

    @get:StringRes
    val errorRes: Int

    /** Returns true if the input is considered valid for some requirement.  */
    fun isValid(input: String): Boolean
}
