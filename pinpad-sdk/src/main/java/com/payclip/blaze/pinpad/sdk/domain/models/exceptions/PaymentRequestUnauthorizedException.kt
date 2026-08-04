package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class PaymentRequestUnauthorizedException :
    Exception("The payment request was rejected because the API credentials are invalid.") {

    companion object {
        const val ERROR_CODE = "PAYMENT_REQUEST_UNAUTHORIZED"
    }
}
