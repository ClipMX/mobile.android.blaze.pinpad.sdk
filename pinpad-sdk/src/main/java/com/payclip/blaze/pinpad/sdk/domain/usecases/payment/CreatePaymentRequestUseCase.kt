package com.payclip.blaze.pinpad.sdk.domain.usecases.payment

import com.payclip.blaze.pinpad.sdk.ClipPaymentSDK
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.SDKNotInitializedException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.request.PaymentRequest
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRequestRepository

internal class CreatePaymentRequestUseCase(
    private val repository: PaymentRequestRepository
) {

    /**
     * Create a payment request in the Clip backend using the credentials settled with
     * [ClipPaymentSDK.initialize]. Never throws, failures are returned as [Result.failure]
     * with a typed exception.
     */
    suspend operator fun invoke(
        reference: String,
        amount: Double,
        tipAmount: Double?,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        preferences: RequestPaymentPreferences
    ): Result<PaymentRequest> {
        val config = ClipPaymentSDK.getConfigOrNull()
            ?: return Result.failure(SDKNotInitializedException())

        return repository.createPaymentRequest(
            environment = config.environment,
            reference = reference,
            amount = amount,
            tipAmount = tipAmount,
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            preferences = preferences
        )
    }
}
