package com.payclip.blaze.pinpad.sdk.data.models.payment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Keep
internal data class CreatePaymentRequestDTO(
    @SerializedName("reference")
    val reference: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("tip_amount")
    val tipAmount: BigDecimal?,
    @SerializedName("serial_number_pos")
    val serialNumberPos: String,
    @SerializedName("preferences")
    val preferences: PaymentRequestPreferencesDTO?
)
