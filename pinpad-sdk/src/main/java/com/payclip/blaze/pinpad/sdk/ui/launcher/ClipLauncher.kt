package com.payclip.blaze.pinpad.sdk.ui.launcher

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

interface ClipLauncher {

    fun setPaymentHandler(
        activity: ComponentActivity,
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    )

    @Composable
    fun setPaymentHandler(
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    )

    fun startPayment(
        requestId: String,
        autoReturn: Boolean = false,
        isTipEnabled: Boolean? = null
    )
}
