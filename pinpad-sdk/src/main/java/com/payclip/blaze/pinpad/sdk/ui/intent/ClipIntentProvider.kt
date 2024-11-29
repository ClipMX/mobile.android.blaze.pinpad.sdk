package com.payclip.blaze.pinpad.sdk.ui.intent

import android.content.Intent
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences

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
     * @param isShareEnabled If it is true, the terminal will you share options in success.
     * If set to false, the terminal will not show share options in success.
     * @param preferences An object loaded with all payment configuration.
     * @param clipLoginCredentials An object loaded with login credentials for the terminal if is selected.
     *
     * @return [Intent] with extras pointing to PinPad application.
     */
    fun getClipIntent(
        reference: String,
        amount: Double,
        isAutoReturnEnabled: Boolean = false,
        isRetryEnabled: Boolean = true,
        isShareEnabled: Boolean = true,
        preferences: PaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin? = null
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
     * Get share buttons availability from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The share buttons availability state. If share availability was not settled, null is returned.
     */
    fun isShareEnabled(intent: Intent): Boolean?

    /**
     * Get payment preferences from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The payment preferences model.
     * If no preferences was settled, a default model is returned.
     */
    fun getPaymentPreferences(intent: Intent): PaymentPreferences

    /**
     * Get login credentials from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The payment login credentials model.
     * If no preferences was settled, a default model is returned and is managed from the Pinpad module.
     */
    fun getPaymentLoginCredentials(intent: Intent): ClipPaymentLogin?
}
