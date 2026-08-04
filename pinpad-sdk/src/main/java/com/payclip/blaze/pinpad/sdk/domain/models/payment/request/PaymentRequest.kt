package com.payclip.blaze.pinpad.sdk.domain.models.payment.request

/**
 * Payment request created in the Clip backend before launching the PinPad application.
 */
internal data class PaymentRequest(
    val requestId: String,
    val userId: String?,
    val reference: String?,
    val amount: String?
)
