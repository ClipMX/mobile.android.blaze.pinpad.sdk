package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class PaymentRequestNetworkException :
    Exception("The payment request could not be created due to a network error.") {

    companion object {
        const val ERROR_CODE = "PAYMENT_REQUEST_NETWORK_ERROR"
    }
}
