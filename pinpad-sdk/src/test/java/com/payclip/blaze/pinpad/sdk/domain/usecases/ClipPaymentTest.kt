package com.payclip.blaze.pinpad.sdk.domain.usecases

import com.payclip.blaze.pinpad.sdk.domain.builder.payment.ClipPayment
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.usecases.payment.CreatePaymentUseCase
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ClipPaymentTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = UnconfinedTestDispatcher()

    private val launcher = mock<ClipLauncher>()

    private val useCase = mock<CreatePaymentUseCase>()

    private val listener = mock<PaymentListener>()

    @Test
    fun `create clip payment with builder, then verify that nothing crash`() = runTest {
        ClipPayment.Builder()
            .isAutoReturnEnabled(AUTO_RETURN)
            .isTipEnabled(IS_TIP_ENABLED)
            .addListener(getEmptyListener())
            .build()
    }

    @Test
    fun `create payment with success response and check if the result is right`() = runTest {
        val payment = getPaymentInstance()

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(REFERENCE, AMOUNT)
    }

    @Test
    fun `create payment with success response and auto return and check if the result is right`() = runTest {
        val payment = getPaymentInstance(autoReturn = true)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(REFERENCE, AMOUNT, autoReturn = true)
    }

    @Test
    fun `create payment with success response and tip enabled and check if the result is right`() = runTest {
        val payment = getPaymentInstance(isTipEnabled = true)

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.success(Unit))

        payment.start(REFERENCE, AMOUNT)

        verify(launcher).startPayment(REFERENCE, AMOUNT, isTipEnabled = true)
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
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance()

        whenever(useCase.invoke(REFERENCE, AMOUNT)).thenReturn(Result.failure(Exception()))

        payment.start(REFERENCE, AMOUNT)

        verify(listener).onFailure(ClipPayment.DEFAULT_ERROR)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    private fun getPaymentInstance(
        autoReturn: Boolean = false,
        isTipEnabled: Boolean? = null
    ) = ClipPayment(
        launcher,
        autoReturn,
        isTipEnabled,
        listener
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

    companion object {
        private const val AMOUNT = 10.0
        private const val REFERENCE = "xyz"

        private const val AUTO_RETURN = false
        private const val IS_TIP_ENABLED = false
    }
}
