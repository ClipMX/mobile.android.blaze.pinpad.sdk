package com.payclip.blaze.pinpad.sdk.data.api.payment

import com.payclip.blaze.pinpad.sdk.data.models.payment.CreatePaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.data.models.payment.PaymentRequestResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface PaymentRequestApi {

    @POST(CREATE_PAYMENT_PATH)
    suspend fun createPaymentRequest(
        @Body request: CreatePaymentRequestDTO
    ): Response<PaymentRequestResponseDTO>

    companion object {
        private const val BASE_PATH = "f2f/pinpad/v1"
        const val CREATE_PAYMENT_PATH = "$BASE_PATH/mobile/payment"
    }
}
