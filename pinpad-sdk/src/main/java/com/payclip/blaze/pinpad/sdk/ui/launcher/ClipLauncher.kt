package com.payclip.blaze.pinpad.sdk.ui.launcher

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

internal interface ClipLauncher {

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling `startPayment`.
     *
     * @param onSuccess success listener called when payment process succeed.
     * @param onCancelled cancelled listener called when payment process is cancelled.
     * @param onFailure failure listener called when payment process fail.
     */
    fun setPaymentHandler(
        activity: ComponentActivity,
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    )

    /**
     * This handler register activity contract in your Composable. It is very import to
     * invoke this method before calling `startPayment`.
     *
     * @param onSuccess success listener called when payment process succeed.
     * @param onCancelled cancelled listener called when payment process is cancelled.
     * @param onFailure failure listener called when payment process fail.
     */
    @Composable
    fun setPaymentHandler(
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    )

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
     *
     * @param requestId The id generated in backend when payment is created.
     * @param autoReturn If it is true, when the payment process throw success or error, you will
     * auto return to your application. Otherwise you will see a defined screen with information.
     * @param isTipEnabled If it is true, you will tip screen before payment start.
     */
    fun startPayment(
        requestId: String,
        autoReturn: Boolean = false,
        isTipEnabled: Boolean? = null
    )
}
