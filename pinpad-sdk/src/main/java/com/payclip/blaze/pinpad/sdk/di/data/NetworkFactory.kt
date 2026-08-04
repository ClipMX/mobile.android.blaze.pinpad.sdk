package com.payclip.blaze.pinpad.sdk.di.data

import com.payclip.blaze.pinpad.sdk.data.api.payment.PaymentRequestApi
import com.payclip.blaze.pinpad.sdk.data.network.AuthInterceptor
import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Builds and caches the Retrofit stack per environment. Timeouts are short on purpose: the
 * payment request creation happens right before launching the PinPad application and must not
 * delay the payment more than a few seconds.
 */
internal object NetworkFactory {

    @Volatile
    private var cache: Pair<ClipEnvironment, PaymentRequestApi>? = null

    fun getPaymentRequestApi(environment: ClipEnvironment): PaymentRequestApi {
        cache?.let { (env, api) -> if (env == environment) return api }

        return synchronized(this) {
            val current = cache
            if (current != null && current.first == environment) {
                current.second
            } else {
                createPaymentRequestApi(environment).also { cache = environment to it }
            }
        }
    }

    private fun createPaymentRequestApi(environment: ClipEnvironment): PaymentRequestApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
            redactHeader(AUTHORIZATION_HEADER)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(environment.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentRequestApi::class.java)
    }

    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val CONNECT_TIMEOUT_SECONDS = 3L
    private const val READ_TIMEOUT_SECONDS = 5L
    private const val CALL_TIMEOUT_SECONDS = 5L
}
