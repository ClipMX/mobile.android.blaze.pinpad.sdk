package com.payclip.blaze.pinpad.sdk.domain.models.payment.settings

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaymentPreferences(
    val isMSIEnabled: Boolean = true,
    val isMCIEnabled: Boolean = true,
    val isDCCEnabled: Boolean = true,
    val isTipEnabled: Boolean = false,
    /**
     * When transaction is successful you can enable the auto print of your receipt in POS
     */
    val isAutoPrintReceiptEnabled: Boolean = false,
    val isSplitPaymentEnabled: Boolean = false,
) : Parcelable {

    companion object : Parceler<PaymentPreferences> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun PaymentPreferences.write(parcel: Parcel, flags: Int) {
            parcel.writeBoolean(isMSIEnabled)
            parcel.writeBoolean(isMCIEnabled)
            parcel.writeBoolean(isDCCEnabled)
            parcel.writeBoolean(isTipEnabled)
            parcel.writeBoolean(isAutoPrintReceiptEnabled)
            parcel.writeBoolean(isSplitPaymentEnabled)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun create(parcel: Parcel): PaymentPreferences =
            PaymentPreferences(
                parcel.readBoolean(),
                parcel.readBoolean(),
                parcel.readBoolean(),
                parcel.readBoolean(),
                parcel.readBoolean(),
                parcel.readBoolean()
            )
    }
}
