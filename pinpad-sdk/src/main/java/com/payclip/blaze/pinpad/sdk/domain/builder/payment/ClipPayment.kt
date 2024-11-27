package com.payclip.blaze.pinpad.sdk.domain.builder.payment

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.di.ui.launcher.ClipLauncherFactory
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.login.LoginResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher

/**
 * With ClipPayment you will be able to start payment and customize your experience as you want.
 */
class ClipPayment internal constructor(
    private val useCase: CreatePaymentUseCase,
    private val launcher: ClipLauncher,
    private val isAutoReturnEnabled: Boolean,
    private val isRetryEnabled: Boolean,
    private val isShareEnabled: Boolean,
    private val preferences: PaymentPreferences,
    private val listener: PaymentListener?,
    private val loginListener: LoginListener?,
    private val loginCredentials: ClipPaymentLogin? = null
) {

    /**
     * [ClipPayment] builder. Offers a way to configure you ClipPayment object and customize your
     * experience.
     */
    class Builder {

        private var isAutoReturnEnabled: Boolean = false

        private var isRetryEnabled: Boolean = true

        private var isShareEnabled: Boolean = true

        private var preferences: PaymentPreferences = PaymentPreferences()

        private var loginCredentials: ClipPaymentLogin? = null

        private var listener: PaymentListener? = null

        private var clipLoginListener: LoginListener? = null

        /**
         * Method to settle return type.
         *
         * @param isEnabled If it is true, when the payment process throw success or error, you will
         * auto return to your application. Otherwise you will see a screen with information.
         */
        fun isAutoReturnEnabled(isEnabled: Boolean) = apply {
            this.isAutoReturnEnabled = isEnabled
        }

        /**
         * Method to settle if retries will be enabled.
         *
         * @param isEnabled If it is true, when the payment process throw error, you will
         * have the chance to retry. Otherwise you will only be able to cancel.
         */
        fun isRetryEnabled(isEnabled: Boolean) = apply {
            this.isRetryEnabled = isEnabled
        }

        /**
         * Method to settle if share buttons will be shown.
         *
         * @param isEnabled If it is true, the terminal will you share options in success.
         * If set to false, the terminal will not show share options in success.
         */
        fun isShareEnabled(isEnabled: Boolean) = apply {
            this.isShareEnabled = isEnabled
        }

        /**
         * Method to settle payment preferences.
         *
         * @param preferences An object loaded with all payment configuration.
         */
        fun setPaymentPreferences(preferences: PaymentPreferences) = apply {
            this.preferences = preferences
        }

        /**
         * Method to settle login credentials.
         *
         * @param loginCredentials An object loaded with all login credentials.
         */
        fun setLoginCredentials(loginCredentials: ClipPaymentLogin) = apply {
            this.loginCredentials = loginCredentials
        }

        /**
         * Method to settle result listener.
         *
         * @param listener Listen the payment process results.
         */
        fun addListener(listener: PaymentListener) = apply {
            this.listener = listener
        }

        /**
         * Method to settle login listener.
         *
         * @param listener Listen the login process results.
         */

        fun addLoginListener(listener: LoginListener) = apply {
            this.clipLoginListener = listener
        }

        /**
         * Method to build ClipPayment object.
         *
         * @return This method returns [ClipPayment] object with all parameters settled.
         */
        fun build(): ClipPayment {
            val useCase = CreatePaymentUseCase()
            val launcher = ClipLauncherFactory.create()

            return ClipPayment(
                useCase = useCase,
                launcher = launcher,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isRetryEnabled = isRetryEnabled,
                isShareEnabled = isShareEnabled,
                preferences = preferences,
                listener = listener,
                loginListener = clipLoginListener,
                loginCredentials = loginCredentials
            )
        }
    }

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling `startPayment`.
     *
     * @param activity component activity needed to register activity contract.
     */
    fun setPaymentHandler(activity: ComponentActivity) {
        launcher.setPaymentHandler(
            activity = activity,
            onSuccess = { listener?.onSuccess(it) },
            onCancelled = { listener?.onCancelled() },
            onFailure = { listener?.onFailure(it) }
        )
    }

    /**
     * This handler register activity contract in your Composable. It is very import to
     * invoke this method before calling `startPayment`.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun setPaymentHandler() {
        launcher.setPaymentHandler(
            onSuccess = { listener?.onSuccess(it) },
            onCancelled = { listener?.onCancelled() },
            onFailure = { listener?.onFailure(it) }
        )
    }

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling `startPayment` and user needs implement login.
     *
     * @param activity component activity needed to register activity contract.
     * */
    fun setLoginHandler(activity: ComponentActivity){
        launcher.setLoginHandler(
            activity = activity,
            onSuccess = { loginResult: LoginResult ->
                loginListener?.onLoginSuccess(loginResult.code)
                        },
            onFailure = { loginResult: LoginResult ->
                loginListener?.onLoginFailure(loginResult.code, loginResult.detail)
                        }
        )
    }

    /**
     * This handler register activity contract in your Activity. It is very import to
     * invoke this method before calling `startPayment` and user needs.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun setLoginHandler(){
        launcher.setLoginHandler(
            onSuccess = { loginResult: LoginResult ->
                loginListener?.onLoginSuccess(loginResult.code)
            },
            onFailure = { loginResult: LoginResult ->
                loginListener?.onLoginFailure(loginResult.code, loginResult.detail)
            }
        )
    }

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     */
    fun start(
        reference: String,
        amount: Double
    ) {
       useCase.invoke(reference = reference, amount = amount)
            .onSuccess {
                launcher.startPayment(
                    reference = reference,
                    amount = amount,
                    isAutoReturnEnabled = isAutoReturnEnabled,
                    isRetryEnabled = isRetryEnabled,
                    isShareEnabled = isShareEnabled,
                    preferences = preferences,
                    clipLoginCredentials = loginCredentials
                )
            }
            .onFailure {
                val code = when(it) {
                    is EmptyAmountException -> EmptyAmountException.ERROR_CODE
                    is EmptyReferenceException -> EmptyReferenceException.ERROR_CODE
                    else -> DEFAULT_ERROR
                }

                listener?.onFailure(code)
            }
    }

    companion object {
        internal const val DEFAULT_ERROR = "UNKNOWN_ERROR"
    }
}
