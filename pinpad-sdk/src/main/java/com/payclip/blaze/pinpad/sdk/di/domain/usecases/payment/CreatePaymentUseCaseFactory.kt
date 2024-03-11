package com.payclip.blaze.pinpad.sdk.di.domain.usecases.payment

import com.payclip.blaze.pinpad.sdk.di.domain.repository.payment.PaymentRepositoryFactory
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase

internal object CreatePaymentUseCaseFactory {

    fun create(
        apiKey: String,
        isDemo: Boolean
    ): CreatePaymentUseCase {
        val repository = PaymentRepositoryFactory.create(apiKey, isDemo)

        return CreatePaymentUseCase(repository)
    }
}
