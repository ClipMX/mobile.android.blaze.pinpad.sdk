package com.payclip.blaze.pinpad.sdk.data.models.payment

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class PaymentRequestPreferencesDTO(
    @SerializedName("is_auto_return_enabled")
    val isAutoReturnEnabled: Boolean,
    @SerializedName("is_retry_enabled")
    val isRetryEnabled: Boolean,
    @SerializedName("is_share_enabled")
    val isShareEnabled: Boolean,
    @SerializedName("is_msi_enabled")
    val isMsiEnabled: Boolean,
    @SerializedName("is_mci_enabled")
    val isMciEnabled: Boolean,
    @SerializedName("is_dcc_enabled")
    val isDccEnabled: Boolean,
    @SerializedName("is_tip_enabled")
    val isTipEnabled: Boolean,
    @SerializedName("is_split_payment_enabled")
    val isSplitPaymentEnabled: Boolean,
    @SerializedName("is_auto_print_receipt_enabled")
    val isAutoPrintReceiptEnabled: Boolean
)
