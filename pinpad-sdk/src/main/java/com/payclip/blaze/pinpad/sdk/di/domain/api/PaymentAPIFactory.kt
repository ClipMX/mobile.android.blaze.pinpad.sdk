package com.payclip.blaze.pinpad.sdk.di.domain.api

import com.payclip.blaze.pinpad.sdk.di.domain.api.retrofit.RetrofitFactory
import com.payclip.blaze.pinpad.sdk.services.api.payment.PaymentAPI

internal object PaymentAPIFactory {

    fun create(
        apiKey: String,
        isDemo: Boolean
    ): PaymentAPI {
        val retrofit = RetrofitFactory.create(apiKey, isDemo)

        return retrofit.create(PaymentAPI::class.java)
    }
}
