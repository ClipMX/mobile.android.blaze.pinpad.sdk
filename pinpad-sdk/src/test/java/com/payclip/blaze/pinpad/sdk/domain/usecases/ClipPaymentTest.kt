package com.payclip.blaze.pinpad.sdk.domain.usecases

import com.payclip.blaze.pinpad.sdk.domain.builder.payment.ClipPayment
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApiKeyNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyMessageException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.UserNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
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
            .setUser(USER)
            .setApiKey(API_KEY)
            .isDemo(IS_DEMO)
            .isAutoReturnEnabled(AUTO_RETURN)
            .addListener(getEmptyListener())
            .setLoadingState(MutableStateFlow(false))
            .build()
    }

    @Test
    fun `create clip payment with builder and not user key, then check if exception is thrown`() = runTest {
        assertThrows(UserNotFoundException::class.java) {
            ClipPayment.Builder()
                .setApiKey(API_KEY)
                .isDemo(IS_DEMO)
                .isAutoReturnEnabled(AUTO_RETURN)
                .addListener(getEmptyListener())
                .setLoadingState(MutableStateFlow(false))
                .build()
        }
    }

    @Test
    fun `create clip payment with builder and not set api key, then check if exception is thrown`() = runTest {
        assertThrows(ApiKeyNotFoundException::class.java) {
            ClipPayment.Builder()
                .setUser(USER)
                .isDemo(IS_DEMO)
                .isAutoReturnEnabled(AUTO_RETURN)
                .addListener(getEmptyListener())
                .setLoadingState(MutableStateFlow(false))
                .build()
        }
    }

    @Test
    fun `create payment with success response and check if the result is right`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(loading = flow)
        val response = getPendingPayment()

        whenever(useCase.invoke(USER, AMOUNT, MESSAGE)).thenReturn(Result.success(response))

        payment.start(AMOUNT, MESSAGE)

        verify(launcher).startPayment(response.requestId, false)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    @Test
    fun `create payment with success response and auto return and check if the result is right`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(autoReturn = true, loading = flow)
        val response = getPendingPayment()

        whenever(useCase.invoke(USER, AMOUNT, MESSAGE)).thenReturn(Result.success(response))

        payment.start(AMOUNT, MESSAGE)

        verify(launcher).startPayment(response.requestId, true)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    @Test
    fun `try to create payment with empty amount and handle thrown exception`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(loading = flow)

        whenever(useCase.invoke(USER, 0.0, MESSAGE)).thenReturn(Result.failure(EmptyAmountException()))
        payment.start(0.0, MESSAGE)

        verify(listener).onFailure(EmptyAmountException.ERROR_CODE)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    @Test
    fun `try to create payment with empty message and handle thrown exception`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(loading = flow)

        whenever(useCase.invoke(USER, AMOUNT, "")).thenReturn(Result.failure(EmptyMessageException()))
        payment.start(AMOUNT, "")

        verify(listener).onFailure(EmptyMessageException.ERROR_CODE)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    @Test
    fun `create payment with error response and check if the error is handled`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(loading = flow)

        whenever(useCase.invoke(USER, AMOUNT, MESSAGE)).thenReturn(Result.failure(CreatePaymentException()))

        payment.start(AMOUNT, MESSAGE)

        verify(listener).onFailure(CreatePaymentException.ERROR_CODE)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    @Test
    fun `create payment with unknown error response and check if the error is handled`() = runTest {
        val results = mutableListOf<Boolean>()
        val flow = MutableStateFlow(false)
        val job = launch(testDispatcher) { flow.toList(results) }
        val payment = getPaymentInstance(loading = flow)

        whenever(useCase.invoke(USER, AMOUNT, MESSAGE)).thenReturn(Result.failure(Exception()))

        payment.start(AMOUNT, MESSAGE)

        verify(listener).onFailure(ClipPayment.DEFAULT_ERROR)
        assertEquals(listOf(false, true, false), results)

        job.cancel()
    }

    private fun getPaymentInstance(
        autoReturn: Boolean = false,
        loading: MutableStateFlow<Boolean>? = null
    ) = ClipPayment(
        launcher,
        useCase,
        USER,
        autoReturn,
        listener,
        loading
    )

    private fun getPendingPayment() = PendingPayment(REQUEST_ID)

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
        private const val USER = "guido.perre@payclip.com"
        private const val MESSAGE = "cena"

        private const val REQUEST_ID = "abc"

        private const val API_KEY = "def"
        private const val IS_DEMO = false
        private const val AUTO_RETURN = false
    }
}
