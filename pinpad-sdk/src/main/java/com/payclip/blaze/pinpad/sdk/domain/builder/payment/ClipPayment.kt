package com.payclip.blaze.pinpad.sdk.domain.builder.payment

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.payclip.blaze.pinpad.sdk.di.domain.usecases.payment.CreatePaymentUseCaseFactory
import com.payclip.blaze.pinpad.sdk.di.ui.launcher.ClipLauncherFactory
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApiKeyNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyMessageException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.UserNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * With ClipPayment you will be able to start payment and customize your experience as you want.
 */
class ClipPayment internal constructor(
    private val launcher: ClipLauncher,
    private val useCase: CreatePaymentUseCase,
    private val user: String,
    private val isAutoReturnEnabled: Boolean,
    private val isTipEnabledEnabled: Boolean?,
    private val listener: PaymentListener?,
    private val isLoading: MutableStateFlow<Boolean>?
) {

    /**
     * [ClipPayment] builder. Offers a way to configure you ClipPayment object and customize your
     * experience.
     */
    class Builder {

        private var user: String? = null

        private var apiKey: String? = null

        private var isDemo: Boolean = false

        private var isAutoReturnEnabled: Boolean = false

        private var isTipEnabledEnabled: Boolean? = null

        private var listener: PaymentListener? = null

        private var isLoading: MutableStateFlow<Boolean>? = null

        /**
         * Method to settle user.
         *
         * @param user Your clip user, it is very important to be logged with this user in PinPad
         * Clip application.
         */
        fun setUser(user: String) = apply { this.user = user }

        /**
         * Method to settle api key.
         *
         * @param apiKey Your clip api key generated in Clip Developer page.
         */
        fun setApiKey(apiKey: String) = apply { this.apiKey = apiKey }

        /**
         * Method to settle environment.
         *
         * @param isDemo If it is true, you will use demo environment. Api keys are different by
         * environment, be sure to change it.
         */
        fun isDemo(isDemo: Boolean) = apply { this.isDemo = isDemo }

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
         * Method to settle tip availability.
         *
         * @param isEnabled If it is true, you will tip screen before payment start.
         */
        fun isTipEnabled(isEnabled: Boolean) = apply {
            this.isTipEnabledEnabled = isEnabled
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
         * Method to settle loading state.
         *
         * @param state A [MutableStateFlow] that handle loading state of request call.
         */
        fun setLoadingState(state: MutableStateFlow<Boolean>) = apply {
            this.isLoading = state
        }

        /**
         * Method to build ClipPayment object.
         *
         * @return This method returns [ClipPayment] object with all parameters settled.
         */
        fun build(): ClipPayment {
            val user = user ?: throw UserNotFoundException()
            val apiKey = apiKey ?: throw ApiKeyNotFoundException()
            val useCase = CreatePaymentUseCaseFactory.create(apiKey, isDemo)
            val launcher = ClipLauncherFactory.create()

            return ClipPayment(
                launcher = launcher,
                useCase = useCase,
                user = user,
                isAutoReturnEnabled = isAutoReturnEnabled,
                isTipEnabledEnabled = isTipEnabledEnabled,
                listener = listener,
                isLoading = isLoading
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
    @Composable
    fun setPaymentHandler() {
        launcher.setPaymentHandler(
            onSuccess = { listener?.onSuccess(it) },
            onCancelled = { listener?.onCancelled() },
            onFailure = { listener?.onFailure(it) }
        )
    }

    /**
     * Call this method when you want to start Clip payment process. Be sure to call
     * `setPaymentHandler` before, otherwise this method call will crash with no initialization
     * exception.
     *
     * @param reference The id or a reference of your payment.
     * @param amount The amount to be processed in payment process.
     * @param message A message you want to add to your payment.
     */
    suspend fun start(
        reference: String,
        amount: Double,
        message: String
    ) {
        isLoading?.value = true
        useCase.invoke(user, reference, amount, message)
            .onSuccess {
                launcher.startPayment(
                    requestId = it.requestId,
                    autoReturn = isAutoReturnEnabled,
                    isTipEnabled = isTipEnabledEnabled
                )
            }
            .onFailure {
                val code = when(it) {
                    is EmptyAmountException -> EmptyAmountException.ERROR_CODE
                    is EmptyMessageException -> EmptyMessageException.ERROR_CODE
                    is CreatePaymentException -> CreatePaymentException.ERROR_CODE
                    else -> DEFAULT_ERROR
                }

                listener?.onFailure(code)
            }
            .also {
                isLoading?.value = false
            }
    }

    companion object {
        internal const val DEFAULT_ERROR = "UNKNOWN_ERROR"
    }
}
