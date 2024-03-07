package com.payclip.blaze.pinpad.sdk.di.domain.api.retrofit

import android.os.Build
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object RetrofitFactory {

    private const val PRODUCTION_CLIP_HOST = "https://api-gw.payclip.com"
    private const val DEMO_CLIP_HOST = "https://testapi-gw.payclip.com"

    private const val TIMEOUT_SECONDS = 30L

    fun create(
        apiKey: String,
        isDemo: Boolean
    ): Retrofit {
        val client = createOkHttpClient(apiKey)
        val url = if (isDemo) DEMO_CLIP_HOST else PRODUCTION_CLIP_HOST

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun createOkHttpClient(apiKey: String): OkHttpClient {
        val headerInterceptor = createHeaderInterceptor(apiKey)

        return OkHttpClient.Builder()
            .addNetworkInterceptor(headerInterceptor)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setupTLS()
            .build()
    }

    private fun createHeaderInterceptor(apiKey: String): Interceptor {
        return Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()

            requestBuilder.header("Accept", "application/vnd.com.payclip.v1+json")
            requestBuilder.header("User-Agent", "Android:(${Build.MODEL}; ${Build.VERSION.SDK_INT}; ${Build.VERSION.RELEASE})")
            requestBuilder.header("x-api-key", "Basic $apiKey")

            chain.proceed(requestBuilder.build())
        }
    }

    private fun OkHttpClient.Builder.setupTLS(): OkHttpClient.Builder {
        val specs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .build()

        return connectionSpecs(listOf(specs))
    }
}
