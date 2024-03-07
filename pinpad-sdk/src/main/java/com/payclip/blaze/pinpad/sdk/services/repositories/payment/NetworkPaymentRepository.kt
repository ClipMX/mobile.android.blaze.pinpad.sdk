package com.payclip.blaze.pinpad.sdk.services.repositories.payment

import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository

internal class NetworkPaymentRepository constructor(
    private val dataSource: PaymentDataSource
) : PaymentRepository {

    override suspend fun create(
        amount: Double,
        user: String,
        message: String
    ): PendingPayment {
        return dataSource.create(amount, user, message)
    }
}
