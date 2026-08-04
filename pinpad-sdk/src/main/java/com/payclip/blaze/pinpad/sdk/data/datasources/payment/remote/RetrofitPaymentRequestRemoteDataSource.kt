package com.payclip.blaze.pinpad.sdk.data.datasources.payment.remote

import com.payclip.blaze.pinpad.sdk.data.api.payment.PaymentRequestApi
import com.payclip.blaze.pinpad.sdk.data.models.payment.CreatePaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.data.models.payment.PaymentRequestResponseDTO
import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestInvalidException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestNetworkException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestServerException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestUnauthorizedException
import java.io.IOException
import java.net.HttpURLConnection

internal class RetrofitPaymentRequestRemoteDataSource(
    private val apiProvider: (ClipEnvironment) -> PaymentRequestApi
) : PaymentRequestRemoteDataSource {

    override suspend fun createPaymentRequest(
        environment: ClipEnvironment,
        request: CreatePaymentRequestDTO
    ): Result<PaymentRequestResponseDTO> {
        return try {
            val response = apiProvider(environment).createPaymentRequest(request)
            val body = response.body()

            when {
                response.isSuccessful && body != null -> Result.success(body)
                response.isSuccessful -> Result.failure(PaymentRequestServerException())
                else -> Result.failure(mapHttpError(response.code()))
            }
        } catch (e: IOException) {
            Result.failure(PaymentRequestNetworkException())
        } catch (e: Exception) {
            Result.failure(PaymentRequestServerException())
        }
    }

    private fun mapHttpError(code: Int): Exception = when (code) {
        HttpURLConnection.HTTP_BAD_REQUEST, HTTP_UNPROCESSABLE_ENTITY ->
            PaymentRequestInvalidException()

        HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_FORBIDDEN ->
            PaymentRequestUnauthorizedException()

        else -> PaymentRequestServerException()
    }

    companion object {
        private const val HTTP_UNPROCESSABLE_ENTITY = 422
    }
}
