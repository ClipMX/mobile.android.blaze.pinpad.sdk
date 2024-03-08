package com.payclip.blaze.pinpad.sdk.services.api.payment.dto

import com.google.gson.annotations.SerializedName

data class PaymentRequestDTO(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("assigned_user")
    val assignedUser: String,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("message")
    val message: String
)
