package com.payclip.blaze.pinpad.sdk.domain.repository.payment

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import kotlin.jvm.Throws

internal interface PaymentRepository {

    @Throws(CreatePaymentException::class)
    suspend fun create(
        user: String,
        reference: String,
        amount: Double,
        message: String
    ): PendingPayment
}
