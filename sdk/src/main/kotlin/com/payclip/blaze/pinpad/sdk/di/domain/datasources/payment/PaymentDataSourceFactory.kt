package com.payclip.blaze.pinpad.sdk.di.domain.datasources.payment

import com.payclip.blaze.pinpad.sdk.di.domain.api.PaymentAPIFactory
import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.services.datasources.payment.ServerPaymentDataSource

internal object PaymentDataSourceFactory {

    fun create(
        apiKey: String,
        isDemo: Boolean
    ): PaymentDataSource {
        val api = PaymentAPIFactory.create(apiKey, isDemo)

        return ServerPaymentDataSource(api)
    }
}
