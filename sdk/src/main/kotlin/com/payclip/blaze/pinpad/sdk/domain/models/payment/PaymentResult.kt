package com.payclip.blaze.pinpad.sdk.domain.models.payment

data class PaymentResult(
    val paymentId: String,
    val merchantId: String,
    val status: String,
    val amount: String
)
