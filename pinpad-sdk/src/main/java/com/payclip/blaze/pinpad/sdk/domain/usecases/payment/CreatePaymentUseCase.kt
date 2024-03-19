package com.payclip.blaze.pinpad.sdk.domain.usecases.payment

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException

internal class CreatePaymentUseCase {

    operator fun invoke(
        reference: String,
        amount: Double
    ): Result<Unit> {
        return try {
            if (reference.isBlank()) {
                throw EmptyReferenceException()
            }

            if (amount == EMPTY_AMOUNT) {
                throw EmptyAmountException()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val EMPTY_AMOUNT = 0.0
    }
}
