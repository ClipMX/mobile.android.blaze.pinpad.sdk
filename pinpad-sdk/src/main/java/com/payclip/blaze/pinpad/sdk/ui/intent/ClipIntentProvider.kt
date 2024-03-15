package com.payclip.blaze.pinpad.sdk.ui.intent

import android.content.Intent

interface ClipIntentProvider {

    /**
     * Get intent with extras generated to start PinPad activity.
     *
     * @param requestId The id generated in backend when payment is created.
     * @param autoReturn If it is true, when the payment process throw success or error, you will
     * auto return to your application. Otherwise you will see a defined screen with information.
     * @param isTipEnabled If it is true, you will tip screen before payment start.
     *
     * @return [Intent] with extras pointing to PinPad application.
     */
    fun getClipIntent(
        requestId: String,
        autoReturn: Boolean = false,
        isTipEnabled: Boolean? = null
    ): Intent

    /**
     * Get request id from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The request id. If it is not a request id an exception will be thrown.
     */
    fun getRequestId(intent: Intent): String

    /**
     * Get auto return from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The auto return state. If auto return was not settled, false is returned.
     */
    fun getAutoReturn(intent: Intent): Boolean

    /**
     * Get tip availability from intent extras.
     *
     * @param intent activity intent with extras.
     *
     * @return The tip availability state. If tip was not settled, null is returned.
     */
    fun isTipEnabled(intent: Intent): Boolean?
}
