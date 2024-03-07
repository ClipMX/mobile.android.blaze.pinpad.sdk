package com.payclip.blaze.pinpad.sdk.services.datasources.payment

import com.payclip.blaze.pinpad.sdk.domain.datasources.payment.PaymentDataSource
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PendingPayment
import com.payclip.blaze.pinpad.sdk.services.api.payment.PaymentAPI
import com.payclip.blaze.pinpad.sdk.services.api.payment.dto.PaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.services.api.payment.dto.PaymentResponseDTO
import java.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ServerPaymentDataSourceTest {

    private lateinit var dataSource: PaymentDataSource

    private val api = mock<PaymentAPI>()

    @Before
    fun setUp() {
        dataSource = ServerPaymentDataSource(api)
    }

    @Test
    fun `make a request to create pending payment and check result when code is 100`() = runTest {
        val code = 100
        val request = getPaymentRequestDTO()
        val response = getPaymentResponseDTO(code)
        val result = getPendingPayment()

        whenever(api.create(request)).thenReturn(response)

        with(dataSource.create(request.amount, request.assignedUser, request.message)) {
            assertEquals(result, this)
        }
    }

    @Test
    fun `make a request to create pending payment and check result when code is 500`() = runTest {
        val code = 500
        val request = getPaymentRequestDTO()
        val response = getPaymentResponseDTO(code)

        whenever(api.create(request)).thenReturn(response)

        assertThrows(Exception::class.java) {
            runBlocking {
                dataSource.create(request.amount, request.assignedUser, request.message)
            }
        }
    }

    private fun getPaymentRequestDTO() = PaymentRequestDTO(
        AMOUNT,
        USER,
        referenceId.toString().substring(0, 11),
        MESSAGE
    )

    private fun getPaymentResponseDTO(code: Int) = PaymentResponseDTO(
        code = code,
        description = DESCRIPTION,
        amount = AMOUNT.toString(),
        message = MESSAGE,
        paymentRequestCode = REQUEST_ID,
        paymentRequestStatus = REQUEST_STATUS
    )

    private fun getPendingPayment() = PendingPayment(REQUEST_ID)

    companion object {
        private val referenceId = UUID(100, 100)
        private val uuid = Mockito.mockStatic(UUID::class.java)

        private const val AMOUNT = 10.0
        private const val USER = "guido.perre@payclip.com"
        private const val MESSAGE = "cena"

        private const val DESCRIPTION = "Saved"
        private const val REQUEST_ID = "abc"
        private const val REQUEST_STATUS = "approved"


        @BeforeClass
        @JvmStatic fun setUpMocks() {
            uuid.`when`<UUID> { UUID.randomUUID() }.thenReturn(referenceId)
        }
    }
}
