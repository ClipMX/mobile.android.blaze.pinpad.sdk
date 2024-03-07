package com.payclip.blaze.pinpad.sdk.domain.usecases

import com.payclip.blaze.pinpad.sdk.di.domain.repository.payment.PaymentRepositoryFactory
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApiKeyNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.UserNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.CancellationException

class ClipPayment internal constructor(
    private val repository: PaymentRepository,
    private val user: String
) {

    data class Builder(
        private var user: String? = null,
        private var apiKey: String? = null,
        private var isDemo: Boolean = false
    ) {

        fun setUser(user: String) = apply { this.user = user }

        fun setApiKey(apiKey: String) = apply { this.apiKey = apiKey }

        fun isDemo(isDemo: Boolean) = apply { this.isDemo = isDemo }

        fun build(): ClipPayment {
            val user = user ?: throw UserNotFoundException()
            val apiKey = apiKey ?: throw ApiKeyNotFoundException()
            val repository = PaymentRepositoryFactory.create(apiKey, isDemo)

            return ClipPayment(repository, user)
        }
    }

    suspend fun create(amount: Double, message: String): Result<PendingPayment> {
        return try {
            if (amount == EMPTY_AMOUNT) {
                throw EmptyAmountException()
            }

            Result.success(repository.create(amount, user, message))
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
