package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class EmptyAmountException : Exception("Amount must not be $0 or empty.") {

    companion object {
        const val ERROR_CODE = "EMPTY_AMOUNT"
    }
}
