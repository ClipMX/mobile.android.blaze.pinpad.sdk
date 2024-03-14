package com.payclip.blaze.pinpad.sdk.domain.builder.payment

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

class ClipPayment internal constructor(
    private val launcher: ClipLauncher,
    private val useCase: CreatePaymentUseCase,
    private val user: String,
    private val isAutoReturnEnabled: Boolean,
    private val isTipEnabledEnabled: Boolean?,
    private val listener: PaymentListener?,
    private val isLoading: MutableStateFlow<Boolean>?
) {

    class Builder {

        private var user: String? = null

        private var apiKey: String? = null

        private var isDemo: Boolean = false

        private var isAutoReturnEnabled: Boolean = false

        private var isTipEnabledEnabled: Boolean? = null

        private var listener: PaymentListener? = null

        private var isLoading: MutableStateFlow<Boolean>? = null

        fun setUser(user: String) = apply { this.user = user }

        fun setApiKey(apiKey: String) = apply { this.apiKey = apiKey }

        fun isDemo(isDemo: Boolean) = apply { this.isDemo = isDemo }

        fun isAutoReturnEnabled(isEnabled: Boolean) = apply {
            this.isAutoReturnEnabled = isEnabled
        }

        fun isTipEnabled(isEnabled: Boolean) = apply {
            this.isTipEnabledEnabled = isEnabled
        }

        fun addListener(listener: PaymentListener) = apply {
            this.listener = listener
        }

        fun setLoadingState(state: MutableStateFlow<Boolean>) = apply {
            this.isLoading = state
        }

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

    @Composable
    fun setPaymentHandler() {
        launcher.setPaymentHandler(
            onSuccess = { listener?.onSuccess(it) },
            onCancelled = { listener?.onCancelled() },
            onFailure = { listener?.onFailure(it) }
        )
    }

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
