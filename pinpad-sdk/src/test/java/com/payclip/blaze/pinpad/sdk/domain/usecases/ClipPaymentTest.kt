package com.payclip.blaze.pinpad.sdk.domain.usecases

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApiKeyNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.CreatePaymentException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.UserNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ClipPaymentTest {

    private lateinit var payment: ClipPayment

    private val repository = mock<PaymentRepository>()

    @Before
    fun setUp() {
        payment = ClipPayment(repository, USER)
    }

    @Test
    fun `create clip payment with builder, then verify that nothing crash`() = runTest {
        ClipPayment.Builder()
            .setUser(USER)
            .setApiKey(API_KEY)
            .isDemo(IS_DEMO)
            .build()
    }

    @Test
    fun `create clip payment with builder and not user key, then check if exception is thrown`() = runTest {
        assertThrows(UserNotFoundException::class.java) {
            ClipPayment.Builder()
                .setApiKey(API_KEY)
                .isDemo(IS_DEMO)
                .build()
        }
    }

    @Test
    fun `create clip payment with builder and not set api key, then check if exception is thrown`() = runTest {
        assertThrows(ApiKeyNotFoundException::class.java) {
            ClipPayment.Builder()
                .setUser(USER)
                .isDemo(IS_DEMO)
                .build()
        }
    }

    @Test
    fun `create payment with success response and check if the result is right`() = runTest {
        val response = getPendingPayment()

        whenever(repository.create(AMOUNT, USER, MESSAGE)).thenReturn(response)

        payment.create(AMOUNT, MESSAGE)
            .onSuccess {
                assertEquals(response, it)
            }
            .onFailure {
                fail()
            }
    }

    @Test
    fun `try to create payment with empty amount and handle thrown exception`() = runTest {
        payment.create(0.0, MESSAGE)
            .onSuccess {
                fail()
            }
            .onFailure {
                assert(it is EmptyAmountException)
            }
    }

    @Test
    fun `create payment with error response and check if the error is handled`() = runTest {
        val exception = CreatePaymentException()

        whenever(repository.create(AMOUNT, USER, MESSAGE)).thenThrow(exception)

        payment.create(AMOUNT, MESSAGE)
            .onSuccess {
                fail()
            }
            .onFailure {
                assertEquals(it, exception)
            }
    }

    private fun getPendingPayment() = PendingPayment(REQUEST_ID)

    companion object {

        private const val AMOUNT = 10.0
        private const val USER = "guido.perre@payclip.com"
        private const val MESSAGE = "cena"

        private const val REQUEST_ID = "abc"

        private const val API_KEY = "def"
        private const val IS_DEMO = false
    }
}
