package com.payclip.blaze.pinpad.sdk.domain.models.payment.settings

import androidx.annotation.Keep

/**
 * Preferences to customize the payment experience in the pinpad app. Every preference is optional,
 * so you only need to settle the ones you want to change.
 *
 * Settle this object with `ClipPayment.Builder.setPaymentPreferences`.
 */
@Keep
data class RequestPaymentPreferences(
    /**
     * Monthly interest-free installments (MSI). When it is true, the cardholder is able to pay in
     * installments without interests if the card is eligible. By default it is enabled.
     */
    val isMSIEnabled: Boolean = true,
    /**
     * Monthly installments with interests (MCI). When it is true, the cardholder is able to pay in
     * installments with interests if the card is eligible. By default it is enabled.
     */
    val isMCIEnabled: Boolean = true,
    /**
     * Dynamic currency conversion (DCC). When it is true, a foreign cardholder is able to pay in
     * the currency of their card. By default it is enabled.
     */
    val isDCCEnabled: Boolean = true,
    /**
     * When it is true, the tip screen is shown when the payment process starts. Otherwise the tip
     * screen is skipped. By default it is disabled.
     */
    val isTipEnabled: Boolean = false,
    /**
     * When it is true, the amount is allowed to be covered with more than one charge. Otherwise the
     * whole amount is charged in a single payment. By default it is disabled.
     */
    val isSplitPaymentEnabled: Boolean = false,
    /**
     * When transaction is successful you can enable the auto print of your receipt in POS. By
     * default it is disabled.
     */
    val isAutoPrintReceiptEnabled: Boolean = false,
    /**
     * Tip percentages to be shown in the tip screen. It only applies when [isTipEnabled] is true.
     * When it is null or empty, the default percentages of the pinpad app will be shown.
     */
    val tipOptions: List<Int>? = null,
    /**
     * Package name of the application to be opened when the payment process finishes. When it is
     * null, the pinpad app returns to the application that started the payment.
     */
    val redirectPackageName: String? = null
)
