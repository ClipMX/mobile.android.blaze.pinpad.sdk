package com.payclip.blaze.pinpad.sdk.domain.models.payment

data class PaymentResult(
    val reference: String,
    val status: String,
    val amount: String,
    val tipAmount: String? = null,
    val receiptNumber: String? = null
)
