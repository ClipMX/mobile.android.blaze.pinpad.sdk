package com.payclip.blaze.pinpad.sdk.domain.repository.payment

import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment
import com.payclip.blaze.pinpad.sdk.domain.models.payment.request.PaymentRequest
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences

internal interface PaymentRequestRepository {

    /**
     * Create a payment request in the Clip backend.
     *
     * This call is intentionally NOT retried: the endpoint has no idempotency key, so retrying
     * a request whose response was lost could create duplicated orders. The payment flow is
     * fail-open, a failure here must degrade to launching the payment without a request id.
     *
     * @return [Result] with the created [PaymentRequest] or a typed exception on failure.
     */
    suspend fun createPaymentRequest(
        environment: ClipEnvironment,
        reference: String,
        amount: Double,
        tipAmount: Double?,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        preferences: RequestPaymentPreferences
    ): Result<PaymentRequest>
}
