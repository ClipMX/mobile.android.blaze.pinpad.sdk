package com.payclip.blaze.pinpad.sdk.services.repositories.payment

import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository

internal class NetworkPaymentRepository constructor(
    private val dataSource: PaymentDataSource
) : PaymentRepository {

    override suspend fun create(
        user: String,
        reference: String,
        amount: Double,
        message: String
    ): PendingPayment {
        return dataSource.create(
            user = user,
            reference = reference,
            amount = amount,
            message = message
        )
    }
}
