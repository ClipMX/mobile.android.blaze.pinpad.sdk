package com.payclip.blaze.pinpad.sdk.domain.models.payment.settings

import androidx.annotation.Keep

@Keep
data class RequestPaymentPreferences(
    val isMSIEnabled: Boolean = true,
    val isMCIEnabled: Boolean = true,
    val isDCCEnabled: Boolean = true,
    val isTipEnabled: Boolean = false,
    val isSplitPaymentEnabled: Boolean = false,
    /**
     * When transaction is successful you can enable the auto print of your receipt in POS
     */
    val isAutoPrintReceiptEnabled: Boolean = false
)
