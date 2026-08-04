package com.payclip.blaze.pinpad.sdk.data.network

import com.payclip.blaze.pinpad.sdk.ClipPaymentSDK
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the merchant `Authorization: Basic <apiToken>` header to every request. The token is
 * read on each intercept so runtime rotation via [ClipPaymentSDK.initialize] is picked up
 * immediately.
 */
internal class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val config = ClipPaymentSDK.getConfigOrNull()
            ?: return chain.proceed(chain.request())

        val request = chain.request().newBuilder()
            .header(AUTHORIZATION_HEADER, config.authHeader)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }
}
