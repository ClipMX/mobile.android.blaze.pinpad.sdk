package com.payclip.blaze.pinpad.sdk.services.repositories.payment

import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.domain.repository.payment.PaymentRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class NetworkPaymentRepositoryTest {

    private lateinit var repository: PaymentRepository

    private val dataSource = mock<PaymentDataSource>()

    @Before
    fun setUp() {
        repository = NetworkPaymentRepository(dataSource)
    }

    @Test
    fun `try to get pending payment and check if it wrap data source correctly`() = runTest {
        val user = "guido.perre@payclip.com"
        val reference = "xyz"
        val amount = 0.1
        val message = "cena"
        val requestId = "abc"
        val response = PendingPayment(requestId)

        whenever(dataSource.create(user, reference, amount, message)).thenReturn(response)

        assertEquals(response, repository.create(user, reference, amount, message))
    }
}
