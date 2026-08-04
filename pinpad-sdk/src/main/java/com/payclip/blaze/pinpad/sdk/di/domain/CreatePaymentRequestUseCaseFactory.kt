package com.payclip.blaze.pinpad.sdk.di.domain

import com.payclip.blaze.pinpad.sdk.di.data.PaymentRequestRepositoryFactory
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentRequestUseCase

internal object CreatePaymentRequestUseCaseFactory {

    fun create(): CreatePaymentRequestUseCase {
        return CreatePaymentRequestUseCase(
            repository = PaymentRequestRepositoryFactory.create()
        )
    }
}
