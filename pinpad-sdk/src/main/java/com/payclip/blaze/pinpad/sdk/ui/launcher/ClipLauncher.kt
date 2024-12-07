package com.payclip.blaze.pinpad.sdk.ui.launcher

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.login.LoginResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences

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
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    )

    /**
     * This handler register activity contract in your Composable. It is very import to
     * invoke this method before calling `startPayment`.
     *
     * @param onSuccess success listener called when payment process succeed.
     * @param onCancelled cancelled listener called when payment process is cancelled.
     * @param onFailure failure listener called when payment process fail.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun setPaymentHandler(
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    )

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling 'startPayment' only if you want to login.
     *  @param activity component activity needed to register activity contract.
     *  @param onSuccess listener called when login process succeed.
     *  @param onFailure listener called when login process fail.
     */
    @Deprecated(message = "We use  only paymentHandler")
    fun setLoginHandler(
        activity: ComponentActivity,
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit,
    )

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling 'startPayment' only if you want to login.
     *  @param onSuccess listener called when login process succeed.
     *  @param onFailure listener called when login process fail.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    @Deprecated(message = "We use  only paymentHandler")
    fun setLoginHandler(
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit
    )

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
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
     * @param clipLoginCredentials An object loaded with login credentials for the terminal if is selected
     */
    fun startPayment(
        reference: String,
        amount: Double,
        isAutoReturnEnabled: Boolean = false,
        isRetryEnabled: Boolean = true,
        isShareEnabled: Boolean = true,
        preferences: PaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin? = null
    )
}
