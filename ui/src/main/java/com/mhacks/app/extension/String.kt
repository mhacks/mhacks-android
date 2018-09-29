package com.mhacks.app.extension

fun String.wrapInQuotes(): String {
    var formattedConfigString: String = this
    if (!startsWith("\"")) {
        formattedConfigString = "\"$formattedConfigString"
    }
    if (!endsWith("\"")) {
        formattedConfigString = "$formattedConfigString\""
    }
    return formattedConfigString
}

fun String.unwrapQuotes(): String {
    var formattedConfigString: String = this
    if (formattedConfigString.startsWith("\"")) {
        formattedConfigString = if (formattedConfigString.length > 1) {
            formattedConfigString.substring(1)
        } else {
            ""
        }
    }
    if (formattedConfigString.endsWith("\"")) {
        formattedConfigString = if (formattedConfigString.length > 1) {
            formattedConfigString.substring(0, formattedConfigString.length - 1)
        } else {
            ""
        }
    }
    return formattedConfigString
}