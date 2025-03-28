package com.payclip.blaze.pinpad.sdk.domain.models.payment.settings

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * This class is deprecated. Use [RequestPaymentPreferences] instead.
 */
@Keep
@Parcelize
@Deprecated(message = "use requestPaymentPreferences instead")
data class PaymentPreferences(
    val isMSIEnabled: Boolean = true,
    val isMCIEnabled: Boolean = true,
    val isDCCEnabled: Boolean = true,
    val isTipEnabled: Boolean = false,
    /**
     * When transaction is successful you can enable the auto print of your receipt in POS
     */
    val isAutoPrintReceiptEnabled: Boolean = false
) : Parcelable
