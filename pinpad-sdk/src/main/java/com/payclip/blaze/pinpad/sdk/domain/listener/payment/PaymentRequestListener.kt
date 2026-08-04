package com.payclip.blaze.pinpad.sdk.domain.listener.payment

/**
 * Listen the result of the payment request creation performed before launching the PinPad
 * application. The payment flow is fail-open: even if the creation fails, the payment is
 * launched anyway, just without a request id attached.
 */
interface PaymentRequestListener {

    /**
     * Called when the payment request was successfully created in the Clip backend.
     *
     * @param requestId The id of the created payment request. It travels to the PinPad
     * application in the payment intent.
     */
    fun onPaymentRequestCreated(requestId: String)

    /**
     * Called when the payment request could not be created. The payment continues without
     * a request id.
     *
     * @param code The error code describing the failure: `SDK_NOT_INITIALIZED`,
     * `PAYMENT_REQUEST_UNAUTHORIZED`, `PAYMENT_REQUEST_INVALID`, `PAYMENT_REQUEST_SERVER_ERROR`
     * or `PAYMENT_REQUEST_NETWORK_ERROR`.
     */
    fun onPaymentRequestFailed(code: String)
}
