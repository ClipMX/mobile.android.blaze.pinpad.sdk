package com.payclip.blaze.pinpad.sdk.data.models.payment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class PaymentRequestResponseDTO(
    @SerializedName("request_id")
    val requestId: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("reference")
    val reference: String?,
    @SerializedName("amount")
    val amount: String?
)
