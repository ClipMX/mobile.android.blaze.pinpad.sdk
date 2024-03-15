package com.payclip.blaze.pinpad.sdk.services.api.payment.dto

import com.google.gson.annotations.SerializedName

internal data class PaymentRequestDTO(
    @SerializedName("assigned_user")
    val assignedUser: String,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("message")
    val message: String
)
