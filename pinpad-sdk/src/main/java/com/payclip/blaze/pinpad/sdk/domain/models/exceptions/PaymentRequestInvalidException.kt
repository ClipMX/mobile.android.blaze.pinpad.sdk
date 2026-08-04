package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class PaymentRequestInvalidException :
    Exception("The payment request was rejected because the request structure is invalid.") {

    companion object {
        const val ERROR_CODE = "PAYMENT_REQUEST_INVALID"
    }
}
