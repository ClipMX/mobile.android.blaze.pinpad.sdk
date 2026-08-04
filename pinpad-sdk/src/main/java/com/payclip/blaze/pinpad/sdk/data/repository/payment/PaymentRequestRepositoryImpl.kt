package com.payclip.blaze.pinpad.sdk.data.repository.payment

import com.payclip.blaze.pinpad.sdk.data.datasources.payment.remote.PaymentRequestRemoteDataSource
import com.payclip.blaze.pinpad.sdk.data.mappers.payment.PaymentRequestMapper
import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestServerException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.request.PaymentRequest
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.providers.serial.SerialNumberProvider
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRequestRepository

internal class PaymentRequestRepositoryImpl(
    private val dataSource: PaymentRequestRemoteDataSource,
    private val mapper: PaymentRequestMapper,
    private val serialNumberProvider: SerialNumberProvider
) : PaymentRequestRepository {

    override suspend fun createPaymentRequest(
        environment: ClipEnvironment,
        reference: String,
        amount: Double,
        tipAmount: Double?,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        preferences: RequestPaymentPreferences
    ): Result<PaymentRequest> {
        val request = mapper.toRequest(
            reference = reference,
            amount = amount,
            tipAmount = tipAmount,
            serialNumber = serialNumberProvider.getSerialNumber(),
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            preferences = preferences
        )

        return dataSource.createPaymentRequest(environment, request).mapCatching { dto ->
            mapper.toDomain(dto) ?: throw PaymentRequestServerException()
        }
    }
}
