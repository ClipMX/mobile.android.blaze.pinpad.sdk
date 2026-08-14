package com.payclip.blaze.pinpad.sdk.domain.models.payment

import androidx.annotation.Keep

@Keep
data class PaymentResult(
    val reference: String,
    val status: String,
    val amount: String,
    val tipAmount: String? = null,
    val receiptNumber: String? = null,
    /**
     * True when the PinPad application had an active session of a DIFFERENT account and it was
     * replaced by the credentials provided through `setLoginCredentials` before processing the
     * payment. False when there was no session, the session already belonged to the provided
     * account, or no credentials were provided.
     */
    val wasSessionReplaced: Boolean = false
)
