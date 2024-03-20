package com.payclip.blaze.pinpad.sdk.domain.builder.payment

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.di.ui.launcher.ClipLauncherFactory
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher

/**
 * With ClipPayment you will be able to start payment and customize your experience as you want.
 */
class ClipPayment internal constructor(
    private val useCase: CreatePaymentUseCase,
    private val launcher: ClipLauncher,
    private val isAutoReturnEnabled: Boolean,
    private val isTipEnabledEnabled: Boolean?,
    private val listener: PaymentListener?
) {

    /**
     * [ClipPayment] builder. Offers a way to configure you ClipPayment object and customize your
     * experience.
     */
    class Builder {

        private var isAutoReturnEnabled: Boolean = false

        private var isTipEnabledEnabled: Boolean? = null

        private var listener: PaymentListener? = null

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
                isTipEnabledEnabled = isTipEnabledEnabled,
                listener = listener
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
                    autoReturn = isAutoReturnEnabled,
                    isTipEnabled = isTipEnabledEnabled
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
