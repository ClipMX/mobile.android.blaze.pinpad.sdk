package com.payclip.blaze.pinpad.sdk.di.data

import com.payclip.blaze.pinpad.sdk.data.datasources.payment.remote.RetrofitPaymentRequestRemoteDataSource
import com.payclip.blaze.pinpad.sdk.data.mappers.payment.PaymentRequestMapper
import com.payclip.blaze.pinpad.sdk.data.providers.serial.RandomSerialNumberProvider
import com.payclip.blaze.pinpad.sdk.data.repository.payment.PaymentRequestRepositoryImpl
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRequestRepository

internal object PaymentRequestRepositoryFactory {

    fun create(): PaymentRequestRepository {
        val dataSource = RetrofitPaymentRequestRemoteDataSource(
            apiProvider = NetworkFactory::getPaymentRequestApi
        )

        return PaymentRequestRepositoryImpl(
            dataSource = dataSource,
            mapper = PaymentRequestMapper(),
            serialNumberProvider = RandomSerialNumberProvider()
        )
    }
}
