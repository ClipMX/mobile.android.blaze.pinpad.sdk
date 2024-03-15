package com.payclip.blaze.pinpad.sdk.services.api.payment.dto

import com.google.gson.annotations.SerializedName

internal data class PaymentResponseDTO(
    @SerializedName("code")
    val code: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("payment_request_code")
    val paymentRequestCode: String,
    @SerializedName("payment_request_status")
    val paymentRequestStatus: String
)
