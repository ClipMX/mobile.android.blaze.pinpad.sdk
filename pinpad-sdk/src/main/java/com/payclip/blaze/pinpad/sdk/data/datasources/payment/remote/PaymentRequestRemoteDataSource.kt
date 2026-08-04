package com.payclip.blaze.pinpad.sdk.data.datasources.payment.remote

import com.payclip.blaze.pinpad.sdk.data.models.payment.CreatePaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.data.models.payment.PaymentRequestResponseDTO
import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment

internal interface PaymentRequestRemoteDataSource {

    suspend fun createPaymentRequest(
        environment: ClipEnvironment,
        request: CreatePaymentRequestDTO
    ): Result<PaymentRequestResponseDTO>
}
