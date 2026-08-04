package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class PaymentRequestServerException :
    Exception("The payment request could not be created due to a server error.") {

    companion object {
        const val ERROR_CODE = "PAYMENT_REQUEST_SERVER_ERROR"
    }
}
