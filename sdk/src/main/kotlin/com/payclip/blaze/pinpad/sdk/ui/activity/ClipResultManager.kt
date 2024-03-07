package com.payclip.blaze.pinpad.sdk.ui.activity

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult

interface ClipResultManager {

    fun setSuccessResult(
        activity: Activity,
        paymentId: String,
        merchantId: String,
        amount: String
    )

    fun setErrorResult(
        activity: Activity,
        paymentId: String,
        merchantId: String,
        amount: String,
        errorCode: String
    )

    fun getResponse(result: ActivityResult): String?

    fun getErrorCode(result: ActivityResult): String?

    fun parseResponse(
        result: ActivityResult,
        response: String,
        onSuccess: (result: PaymentResult) -> Unit,
        onFailure: (code: String) -> Unit
    )
}
