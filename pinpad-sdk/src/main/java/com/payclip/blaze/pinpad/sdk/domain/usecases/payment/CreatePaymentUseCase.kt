package com.payclip.blaze.pinpad.sdk.domain.usecases.payment

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyMessageException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.CancellationException

internal class CreatePaymentUseCase constructor(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke(
        user: String,
        reference: String,
        amount: Double,
        message: String
    ): Result<PendingPayment> {
        return try {
            if (amount == EMPTY_AMOUNT) {
                throw EmptyAmountException()
            }

            if (message.isEmpty()) {
                throw EmptyMessageException()
            }

            Result.success(
                repository.create(
                    user = user,
                    reference = reference,
                    amount = amount,
                    message = message
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val EMPTY_AMOUNT = 0.0
    }
}
