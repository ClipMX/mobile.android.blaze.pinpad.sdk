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
        user: String,
        reference: String,
        amount: Double,
        message: String
    ): PendingPayment {
        val request = getRequest(user, reference, amount, message)
        val response = api.create(request)

        if (response.code != 100) {
            throw CreatePaymentException()
        } else {
            return PendingPayment(response.paymentRequestCode)
        }
    }

    private fun getRequest(
        user: String,
        reference: String,
        amount: Double,
        message: String
    ) = PaymentRequestDTO(
        assignedUser = user,
        reference = reference,
        amount = amount,
        message = message
    )
}
