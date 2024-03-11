package com.payclip.blaze.pinpad.sdk.domain.listener.payment

import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

interface PaymentListener {

    fun onSuccess(result: PaymentResult)

    fun onCancelled()

    fun onFailure(code: String)
}
