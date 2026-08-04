package com.payclip.blaze.pinpad.sdk.data.mappers.payment

import com.payclip.blaze.pinpad.sdk.data.models.payment.CreatePaymentRequestDTO
import com.payclip.blaze.pinpad.sdk.data.models.payment.PaymentRequestPreferencesDTO
import com.payclip.blaze.pinpad.sdk.data.models.payment.PaymentRequestResponseDTO
import com.payclip.blaze.pinpad.sdk.domain.models.payment.request.PaymentRequest
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import java.math.BigDecimal
import java.math.RoundingMode

internal class PaymentRequestMapper {

    fun toRequest(
        reference: String,
        amount: Double,
        tipAmount: Double?,
        serialNumber: String,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        preferences: RequestPaymentPreferences
    ): CreatePaymentRequestDTO = CreatePaymentRequestDTO(
        reference = reference,
        amount = amount.toMoney(),
        tipAmount = tipAmount?.toMoney(),
        serialNumberPos = serialNumber,
        preferences = PaymentRequestPreferencesDTO(
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            isMsiEnabled = preferences.isMSIEnabled,
            isMciEnabled = preferences.isMCIEnabled,
            isDccEnabled = preferences.isDCCEnabled,
            isTipEnabled = preferences.isTipEnabled,
            isSplitPaymentEnabled = preferences.isSplitPaymentEnabled,
            isAutoPrintReceiptEnabled = preferences.isAutoPrintReceiptEnabled
        )
    )

    fun toDomain(dto: PaymentRequestResponseDTO): PaymentRequest? {
        val requestId = dto.requestId?.takeIf { it.isNotBlank() } ?: return null

        return PaymentRequest(
            requestId = requestId,
            userId = dto.userId,
            reference = dto.reference,
            amount = dto.amount
        )
    }

    private fun Double.toMoney(): BigDecimal =
        BigDecimal.valueOf(this).setScale(MONEY_SCALE, RoundingMode.HALF_UP)

    companion object {
        private const val MONEY_SCALE = 2
    }
}
