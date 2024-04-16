package com.payclip.blaze.pinpad.sdk.ui.intent

import android.content.Intent
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.ui.intent.system.SystemClipIntentProvider

interface ClipIntentProvider {

    /**
     * Get intent with extras generated to start PinPad activity.
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     * @param isAutoReturnEnabled If it is true, when the payment process throw success or error, you will
     * auto return to your application. Otherwise you will see a defined screen with information.
     * @param isRetryEnabled If it is true, when the payment process throw error, you will
     * have the chance to retry. Otherwise you will only be able to cancel.
     * @param preferences An object loaded with all payment configuration.
     *
     * @return [Intent] with extras pointing to PinPad application.
     */
    fun getClipIntent(
        reference: String,
        amount: Double,
        isAutoReturnEnabled: Boolean = false,
        isRetryEnabled: Boolean = true,
        preferences: PaymentPreferences
    ): Intent

    /**
     * Get merchant payment reference from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The merchant payment reference.
     */
    fun getReference(intent: Intent): String?

    /**
     * Get amount to be charged from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The amount to be charged.
     */
    fun getAmount(intent: Intent): String?

    /**
     * Get auto return from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The auto return state. If auto return was not settled, null is returned.
     */
    fun isAutoReturnEnabled(intent: Intent): Boolean?

    /**
     * Get retry availability from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The retry availability state. If retry availability was not settled, null is returned.
     */
    fun isRetryEnabled(intent: Intent): Boolean?

    /**
     * Get payment preferences from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The payment preferences model.
     * If no preferences was settled, a default model is returned.
     */
    fun getPaymentPreferences(intent: Intent): PaymentPreferences
}
