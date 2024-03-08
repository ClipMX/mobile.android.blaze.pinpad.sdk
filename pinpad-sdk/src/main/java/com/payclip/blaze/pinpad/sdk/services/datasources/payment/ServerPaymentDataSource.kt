package com.payclip.blaze.pinpad.sdk.services.datasources.payment

import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.services.api.payment.PaymentAPI
import com.payclip.blaze.pinpad.sdk.services.api.payment.dto.PaymentRequestDTO
import java.util.UUID

internal class ServerPaymentDataSource constructor(
    private val api: PaymentAPI
) : PaymentDataSource {

    override suspend fun create(
        amount: Double,
        user: String,
        message: String
    ): PendingPayment {
        val request = getRequest(amount, user, message)
        val response = api.create(request)

        if (response.code != 100) {
            throw CreatePaymentException()
        } else {
            return PendingPayment(response.paymentRequestCode)
        }
    }

    private fun getRequest(
        amount: Double,
        user: String,
        message: String
    ) = PaymentRequestDTO(
        amount,
        user,
        UUID.randomUUID().toString().substring(0, 11),
        message
    )
}
