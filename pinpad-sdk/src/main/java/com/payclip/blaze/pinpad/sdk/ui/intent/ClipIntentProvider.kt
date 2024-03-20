package com.payclip.blaze.pinpad.sdk.ui.intent

import android.content.Intent

interface ClipIntentProvider {

    /**
     * Get intent with extras generated to start PinPad activity.
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     * @param autoReturn If it is true, when the payment process throw success or error, you will
     * auto return to your application. Otherwise you will see a defined screen with information.
     * @param isTipEnabled If it is true, you will tip screen before payment start.
     *
     * @return [Intent] with extras pointing to PinPad application.
     */
    fun getClipIntent(
        reference: String,
        amount: Double,
        autoReturn: Boolean = false,
        isTipEnabled: Boolean? = null
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
    fun getAutoReturn(intent: Intent): Boolean?

    /**
     * Get tip availability from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The tip availability state. If tip was not settled, null is returned.
     */
    fun isTipEnabled(intent: Intent): Boolean?
}
