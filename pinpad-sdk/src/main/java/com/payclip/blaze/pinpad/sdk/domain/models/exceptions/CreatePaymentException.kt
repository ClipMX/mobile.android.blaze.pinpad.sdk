package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class CreatePaymentException : Exception("Something went wrong creating the payment.") {

    companion object {
        const val ERROR_CODE = "SERVICE_ERROR"
    }
}
