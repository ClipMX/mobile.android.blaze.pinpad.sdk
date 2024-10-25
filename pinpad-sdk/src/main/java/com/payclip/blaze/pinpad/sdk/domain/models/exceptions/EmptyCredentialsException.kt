package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class EmptyCredentialsException: Exception("Email and password must not be empty values") {

    companion object {
        const val ERROR_CODE = "EMPTY_CREDENTIALS"
    }
}