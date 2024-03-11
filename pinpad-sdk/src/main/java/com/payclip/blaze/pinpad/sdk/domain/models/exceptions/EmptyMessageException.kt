package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class EmptyMessageException : Exception("Message must not be empty.") {

    companion object {
        const val ERROR_CODE = "EMPTY_MESSAGE"
    }
}
