package com.payclip.blaze.pinpad.sdk.domain.models.payment.settings

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentPreferences(
    val isQPSEnabled: Boolean = false,
    val isMSIEnabled: Boolean = true,
    val isMCIEnabled: Boolean = true,
    val isDCCEnabled: Boolean = false,
    val isTipEnabled: Boolean = false
) : Parcelable
