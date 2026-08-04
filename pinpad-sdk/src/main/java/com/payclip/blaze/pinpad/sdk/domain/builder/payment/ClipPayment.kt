package com.payclip.blaze.pinpad.sdk.domain.builder.payment

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.ClipPaymentSDK
import com.payclip.blaze.pinpad.sdk.di.domain.CreatePaymentRequestUseCaseFactory
import com.payclip.blaze.pinpad.sdk.di.ui.launcher.ClipLauncherFactory
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentRequestListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApplicationNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentListenerInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestInvalidException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestNetworkException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestServerException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentRequestUnauthorizedException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.SDKNotInitializedException
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentRequestUseCase
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * With ClipPayment you will be able to start payment and customize your experience as you want.
 */
class ClipPayment internal constructor(
    private val useCase: CreatePaymentUseCase,
    private val createPaymentRequestUseCase: CreatePaymentRequestUseCase,
    private val launcher: ClipLauncher,
    private val isAutoReturnEnabled: Boolean,
    private val isRetryEnabled: Boolean,
    private val isShareEnabled: Boolean,
    private val preferences: RequestPaymentPreferences,
    private val listener: PaymentListener?,
    private val loginListener: LoginListener?,
    private val loginCredentials: ClipPaymentLogin? = null,
    private val paymentRequestListener: PaymentRequestListener? = null
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    /**
     * [ClipPayment] builder. Offers a way to configure you ClipPayment object and customize your
     * experience.
     */
    class Builder {

        private var isAutoReturnEnabled: Boolean = false

        private var isRetryEnabled: Boolean = true

        private var isShareEnabled: Boolean = true

        private var preferences: RequestPaymentPreferences = RequestPaymentPreferences()

        private var loginCredentials: ClipPaymentLogin? = null

        private var listener: PaymentListener? = null

        private var clipLoginListener: LoginListener? = null

        private var paymentRequestListener: PaymentRequestListener? = null

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
        fun setPaymentPreferences(preferences: RequestPaymentPreferences) = apply {
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
         * Method to settle payment request listener.
         *
         * @param listener Listen the result of the payment request creation performed against
         * the Clip backend before launching the PinPad application. Requires the SDK to be
         * initialized with [ClipPaymentSDK.initialize].
         */
        fun addPaymentRequestListener(listener: PaymentRequestListener) = apply {
            this.paymentRequestListener = listener
        }

        /**
         * Method to build ClipPayment object.
         *
         * @return This method returns [ClipPayment] object with all parameters settled.
         */
        fun build(): ClipPayment {
            val useCase = CreatePaymentUseCase()
            val createPaymentRequestUseCase = CreatePaymentRequestUseCaseFactory.create()
            val launcher = ClipLauncherFactory.create()

            return ClipPayment(
                useCase = useCase,
                createPaymentRequestUseCase = createPaymentRequestUseCase,
                launcher = launcher,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isRetryEnabled = isRetryEnabled,
                isShareEnabled = isShareEnabled,
                preferences = preferences,
                listener = listener,
                loginListener = clipLoginListener,
                loginCredentials = loginCredentials,
                paymentRequestListener = paymentRequestListener
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
        if (listener != null) {
            launcher.setPaymentHandler(
                activity = activity,
                paymentListener = listener,
                loginListener = loginListener
            )
        } else {
            throw PaymentListenerInitializationException()
        }
    }

    /**
     * This handler register activity contract in your Composable. It is very import to
     * invoke this method before calling `startPayment`.
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun setPaymentHandler() {
        if (listener != null) {
            launcher.setPaymentHandler(
                paymentListener = listener,
                loginListener = loginListener
            )
        } else {
            throw PaymentListenerInitializationException()
        }
    }

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
     *
     * If the SDK was initialized with [ClipPaymentSDK.initialize], a payment request is created
     * in the Clip backend before launching the PinPad application and its id travels with the
     * payment intent. The flow is fail-open: if the creation fails, the payment starts anyway
     * without a request id and the failure is notified to the [PaymentRequestListener].
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     */
    fun start(
        reference: String,
        amount: Double
    ) {
        start(reference = reference, amount = amount, tipAmount = null)
    }

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
     *
     * If the SDK was initialized with [ClipPaymentSDK.initialize], a payment request is created
     * in the Clip backend before launching the PinPad application and its id travels with the
     * payment intent. The flow is fail-open: if the creation fails, the payment starts anyway
     * without a request id and the failure is notified to the [PaymentRequestListener].
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     * @param tipAmount The tip amount to be processed in payment process.
     */
    fun start(
        reference: String,
        amount: Double,
        tipAmount: Double
    ) {
        start(reference = reference, amount = amount, tipAmount = tipAmount as Double?)
    }

    private fun start(
        reference: String,
        amount: Double,
        tipAmount: Double?
    ) {
        useCase.invoke(reference = reference, amount = amount)
            .onSuccess {
                if (ClipPaymentSDK.isInitialized()) {
                    startWithPaymentRequest(reference, amount, tipAmount)
                } else {
                    Log.w(TAG, SDK_NOT_INITIALIZED_WARNING)
                    paymentRequestListener?.onPaymentRequestFailed(SDKNotInitializedException.ERROR_CODE)
                    launchPayment(reference, amount, tipAmount, requestId = null)
                }
            }
            .onFailure {
                val code = when (it) {
                    is EmptyAmountException -> EmptyAmountException.ERROR_CODE
                    is EmptyReferenceException -> EmptyReferenceException.ERROR_CODE
                    else -> DEFAULT_ERROR
                }

                listener?.onFailure(code)
            }
    }

    private fun startWithPaymentRequest(
        reference: String,
        amount: Double,
        tipAmount: Double?
    ) {
        scope.launch {
            val requestId = createPaymentRequestOrNull(reference, amount, tipAmount)

            try {
                launchPayment(reference, amount, tipAmount, requestId)
            } catch (e: ApplicationNotFoundException) {
                // Thrown inside a coroutine it would crash the host application instead of
                // reaching the integrator's try/catch, so it is delivered as a callback.
                listener?.onFailure(APPLICATION_NOT_FOUND_ERROR)
            }
        }
    }

    private suspend fun createPaymentRequestOrNull(
        reference: String,
        amount: Double,
        tipAmount: Double?
    ): String? {
        val result = withTimeoutOrNull(PAYMENT_REQUEST_TIMEOUT_MS) {
            createPaymentRequestUseCase.invoke(
                reference = reference,
                amount = amount,
                tipAmount = tipAmount,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isRetryEnabled = isRetryEnabled,
                isShareEnabled = isShareEnabled,
                preferences = preferences
            )
        } ?: Result.failure(PaymentRequestNetworkException())

        return result.fold(
            onSuccess = { request ->
                paymentRequestListener?.onPaymentRequestCreated(request.requestId)
                request.requestId
            },
            onFailure = { error ->
                paymentRequestListener?.onPaymentRequestFailed(error.toErrorCode())
                null
            }
        )
    }

    private fun launchPayment(
        reference: String,
        amount: Double,
        tipAmount: Double?,
        requestId: String?
    ) {
        if (tipAmount != null) {
            launcher.startPayment(
                reference = reference,
                amount = amount,
                tipAmount = tipAmount,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isRetryEnabled = isRetryEnabled,
                isShareEnabled = isShareEnabled,
                requestPaymentPreferences = preferences,
                clipLoginCredentials = loginCredentials,
                requestId = requestId
            )
        } else {
            launcher.startPayment(
                reference = reference,
                amount = amount,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isRetryEnabled = isRetryEnabled,
                isShareEnabled = isShareEnabled,
                requestPaymentPreferences = preferences,
                clipLoginCredentials = loginCredentials,
                requestId = requestId
            )
        }
    }

    private fun Throwable.toErrorCode(): String = when (this) {
        is SDKNotInitializedException -> SDKNotInitializedException.ERROR_CODE
        is PaymentRequestUnauthorizedException -> PaymentRequestUnauthorizedException.ERROR_CODE
        is PaymentRequestInvalidException -> PaymentRequestInvalidException.ERROR_CODE
        is PaymentRequestNetworkException -> PaymentRequestNetworkException.ERROR_CODE
        is PaymentRequestServerException -> PaymentRequestServerException.ERROR_CODE
        else -> DEFAULT_ERROR
    }

    companion object {
        internal const val DEFAULT_ERROR = "UNKNOWN_ERROR"
        internal const val APPLICATION_NOT_FOUND_ERROR = "APPLICATION_NOT_FOUND"
        private const val PAYMENT_REQUEST_TIMEOUT_MS = 5_000L
        private const val TAG = "ClipPayment"
        private const val SDK_NOT_INITIALIZED_WARNING =
            "ClipPaymentSDK.initialize(...) has not been called. The payment will start " +
                "without a payment request attached."
    }
}
