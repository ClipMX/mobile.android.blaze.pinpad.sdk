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

        private const val VERSION_1_0_9_1 = 1
        private const val VERSION_1_0_10_X = 2
        private const val CURRENT_VERSION = VERSION_1_0_9_1

        override fun PaymentPreferences.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(CURRENT_VERSION) // Write the version first
            parcel.writeInt(if (isMSIEnabled) 1 else 0)
            parcel.writeInt(if (isMCIEnabled) 1 else 0)
            parcel.writeInt(if (isDCCEnabled) 1 else 0)
            parcel.writeInt(if (isTipEnabled) 1 else 0)
            parcel.writeInt(if (isAutoPrintReceiptEnabled) 1 else 0)
            if (CURRENT_VERSION >= VERSION_1_0_10_X) {
                parcel.writeInt(if (isSplitPaymentEnabled) 1 else 0)
            } else {
                parcel.writeInt(0)
            }
        }

        override fun create(parcel: Parcel): PaymentPreferences {
            val version = parcel.readInt() // Read the version first
            return PaymentPreferences(
                parcel.readInt() == 1,
                parcel.readInt() == 1,
                parcel.readInt() == 1,
                parcel.readInt() == 1,
                parcel.readInt() == 1,
                if (version >= VERSION_1_0_10_X) {
                    parcel.readInt() == 1
                } else {
                    false // Default to false for older versions
                }
            )
        }
    }
}
