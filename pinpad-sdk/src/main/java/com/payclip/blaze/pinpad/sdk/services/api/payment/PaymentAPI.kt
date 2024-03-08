package com.payclip.blaze.pinpad.sdk.services.api.payment

import com.payclip.blaze.pinpad.sdk.services.api.payment.dto.PaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.services.api.payment.dto.PaymentResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

internal interface PaymentAPI {

    @POST(PATH)
    suspend fun create(
        @Body request: PaymentRequestDTO
    ): PaymentResponseDTO

    companion object {
        private const val PATH = "paymentrequest"
    }
}
