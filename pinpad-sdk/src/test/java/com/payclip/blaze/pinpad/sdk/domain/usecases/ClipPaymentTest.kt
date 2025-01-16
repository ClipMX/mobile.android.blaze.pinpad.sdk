package com.payclip.blaze.pinpad.sdk.domain.usecases

import com.payclip.blaze.pinpad.sdk.domain.builder.payment.ClipPayment
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ClipPaymentTest {

    private val launcher = mock<ClipLauncher>()

    private val useCase = mock<CreatePaymentUseCase>()

    private val listener = mock<PaymentListener>()

    private val loginListener = mock<LoginListener>()

    @Test
    fun `create clip payment with builder, then verify that nothing crash`() = runTest {
        ClipPayment.Builder()
            .isAutoReturnEnabled(AUTO_RETURN)
            .isRetryEnabled(RETRY)
            .isShareEnabled(SHARE)
            .setPaymentPreferences(getPaymentPreferences())
            .setLoginCredentials(getLoginCredentialsTest())
            .addListener(getEmptyListener())
            .addLoginListener(getEmptyLoginListener())
            .build()
    }

    @Test
    fun `create payment with success response and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences()
        val payment = getPaymentInstance()

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and auto return and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences()
        val payment = getPaymentInstance(isAutoReturnEnabled = true)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = true,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and retries disabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences()
        val payment = getPaymentInstance(isRetryEnabled = false)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = false,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and share options disabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences()
        val payment = getPaymentInstance(isShareEnabled = false)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            isShareEnabled = false,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and msi disabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences(isMSIEnabled = true)
        val payment = getPaymentInstance(preferences = preferences)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and mci disabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences(isMCIEnabled = true)
        val payment = getPaymentInstance(preferences = preferences)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and dcc disabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences(isDCCEnabled = true)
        val payment = getPaymentInstance(preferences = preferences)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `create payment with success response and tip enabled and check if the result is right`() = runTest {
        val preferences = getPaymentPreferences(isTipEnabled = true)
        val payment = getPaymentInstance(preferences = preferences)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(
            reference = REFERENCE,
            amount = AMOUNT,
            isAutoReturnEnabled = false,
            isRetryEnabled = true,
            preferences = preferences
        )
    }

    @Test
    fun `try to create payment with empty amount and handle thrown exception`() = runTest {
        val payment = getPaymentInstance()

        whenever(useCase.invoke(REFERENCE, 0.0)).thenReturn(Result.failure(EmptyAmountException()))
        payment.start(REFERENCE, 0.0)

        verify(listener).onFailure(EmptyAmountException.ERROR_CODE)
    }

    @Test
    fun `try to create payment with empty message and handle thrown exception`() = runTest {
        val payment = getPaymentInstance()

        whenever(useCase.invoke("", AMOUNT)).thenReturn(Result.failure(EmptyReferenceException()))
        payment.start("", AMOUNT)

        verify(listener).onFailure(EmptyReferenceException.ERROR_CODE)
    }

    @Test
    fun `create payment with unknown error response and check if the error is handled`() = runTest {
        val payment = getPaymentInstance()

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.failure(Exception()))

        payment.start(REFERENCE, AMOUNT)

        verify(listener).onFailure(ClipPayment.DEFAULT_ERROR)
    }

    private fun getPaymentInstance(
        isAutoReturnEnabled: Boolean = false,
        isRetryEnabled: Boolean = true,
        isShareEnabled: Boolean = true,
        isSplitPaymentEnabled : Boolean = false,
        preferences: PaymentPreferences = getPaymentPreferences()
    ) = ClipPayment(
        useCase,
        launcher,
        isAutoReturnEnabled,
        isRetryEnabled,
        isShareEnabled,
        isSplitPaymentEnabled,
        preferences,
        listener,
        loginListener
    )

    private fun getEmptyListener() = object : PaymentListener {
        override fun onSuccess(result: PaymentResult) {
            // no-op
        }

        override fun onCancelled() {
            // no-op
        }

        override fun onFailure(code: String) {
            // no-op
        }
    }

    private fun getEmptyLoginListener() = object : LoginListener {
        override fun onLoginSuccess(result: String) {
            // no-op
        }
        override fun onLoginFailure(code: String, message: String?) {
            // no-op
        }
    }

    private fun getPaymentPreferences(
        isMSIEnabled: Boolean = IS_MSI_ENABLED,
        isMCIEnabled: Boolean = IS_MCI_ENABLED,
        isDCCEnabled: Boolean = IS_DCC_ENABLED,
        isTipEnabled: Boolean = IS_TIP_ENABLED
    ) = PaymentPreferences(
        isMSIEnabled = isMSIEnabled,
        isMCIEnabled = isMCIEnabled,
        isDCCEnabled = isDCCEnabled,
        isTipEnabled = isTipEnabled
    )

    private fun getLoginCredentialsTest() = ClipPaymentLogin(
        userAccount = "",
        passwordAccount = ""
    )

    companion object {
        private const val AMOUNT = 10.0
        private const val REFERENCE = "xyz"

        private const val AUTO_RETURN = false
        private const val RETRY = true
        private const val SHARE = true

        private const val IS_MSI_ENABLED = true
        private const val IS_MCI_ENABLED = true
        private const val IS_DCC_ENABLED = true
        private const val IS_TIP_ENABLED = false
    }
}
