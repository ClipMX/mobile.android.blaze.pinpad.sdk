package com.payclip.blaze.pinpad.sdk.domain.models.payment.login

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ClipPaymentLogin (
    val userAccount : String = "",
    val passwordAccount : String = ""
) : Parcelable