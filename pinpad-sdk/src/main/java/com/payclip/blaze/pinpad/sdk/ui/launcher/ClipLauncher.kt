package com.payclip.blaze.pinpad.sdk.ui.launcher

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

interface ClipLauncher {

    @Composable
    fun setPaymentHandler(
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    ): ManagedActivityResultLauncher<Intent, ActivityResult>

    fun startPayment(
        requestId: String,
        autoReturn: Boolean = false,
        isTipEnabled: Boolean = false
    )
}
