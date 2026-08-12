package com.payclip.blaze.pinpad.sdk.domain.listener.payment

import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

interface PaymentListener {

    fun onSuccess(result: PaymentResult)

    fun onCancelled()

    fun onFailure(code: String)

    /**
     * Invoked on payment failure with the full [PaymentResult] when it is available, so fields
     * like [PaymentResult.wasSessionReplaced] can be read on declined payments too. The default
     * implementation delegates to [onFailure] to keep existing integrations working.
     *
     * @param code The error code thrown by the payment process.
     * @param result The payment result associated with the failure, or null when the response
     * could not be parsed or the failure happened before reaching the PinPad application.
     */
    fun onFailure(code: String, result: PaymentResult?) = onFailure(code)
}
