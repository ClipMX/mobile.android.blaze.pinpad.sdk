package com.payclip.blaze.pinpad.sdk.di.domain.repository.payment

import com.payclip.blaze.pinpad.sdk.di.domain.datasources.payment.PaymentDataSourceFactory
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository
import com.payclip.blaze.pinpad.sdk.services.repositories.payment.NetworkPaymentRepository

internal object PaymentRepositoryFactory {

    fun create(
        apiKey: String,
        isDemo: Boolean
    ): PaymentRepository {
        val dataSource = PaymentDataSourceFactory.create(apiKey, isDemo)

        return NetworkPaymentRepository(dataSource)
    }
}
