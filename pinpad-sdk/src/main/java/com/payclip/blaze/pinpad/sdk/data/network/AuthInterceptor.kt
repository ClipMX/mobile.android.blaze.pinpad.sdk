package com.payclip.blaze.pinpad.sdk.data.network

import android.util.Base64
import com.payclip.blaze.pinpad.sdk.ClipPaymentSDK
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the merchant `Authorization: Basic base64(apiKey:secretKey)` header to every request.
 * Credentials are read on each intercept so runtime rotation via
 * [ClipPaymentSDK.initialize] is picked up immediately.
 */
internal class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val config = ClipPaymentSDK.getConfigOrNull()
            ?: return chain.proceed(chain.request())

        val credentials = "${config.apiKey}:${config.secretKey}"
        val token = Base64.encodeToString(credentials.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)

        val request = chain.request().newBuilder()
            .header(AUTHORIZATION_HEADER, "$BASIC_PREFIX$token")
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BASIC_PREFIX = "Basic "
    }
}
