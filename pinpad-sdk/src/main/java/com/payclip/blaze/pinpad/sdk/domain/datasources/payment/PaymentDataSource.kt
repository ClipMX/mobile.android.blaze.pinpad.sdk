package com.payclip.blaze.pinpad.sdk.domain.datasources.payment

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import kotlin.jvm.Throws

internal interface PaymentDataSource {

    @Throws(CreatePaymentException::class)
    suspend fun create(
        user: String,
        reference: String,
        amount: Double,
        message: String
    ): PendingPayment
}
