package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class EmptyReferenceException : Exception("Reference must not be empty.") {

    companion object {
        const val ERROR_CODE = "EMPTY_REFERENCE"
    }
}
