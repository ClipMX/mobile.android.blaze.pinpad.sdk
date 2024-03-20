package com.payclip.blaze.pinpad.sdk.ui.activity.result

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager

class ActivityClipResultManager : ClipResultManager {

    override fun setSuccessResult(
        activity: Activity,
        reference: String,
        amount: String
    ) {
        val result = PaymentResult(
            reference = reference,
            status = PAYMENT_RESPONSE_APPROVED_STATUS,
            amount = amount
        )

        activity.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(
                    PAYMENT_RESPONSE_EXTRA,
                    Gson().toJson(result)
                )
            }
        )

        activity.finish()
    }

    override fun setErrorResult(
        activity: Activity,
        reference: String,
        amount: String,
        errorCode: String
    ) {
        val result = PaymentResult(
            reference = reference,
            status = PAYMENT_RESPONSE_ERROR_STATUS,
            amount = amount
        )

        activity.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(PAYMENT_RESPONSE_EXTRA, Gson().toJson(result))
                putExtra(PAYMENT_ERROR_EXTRA, errorCode)
            }
        )

        activity.finish()
    }

    override fun getResponse(result: ActivityResult): String? {
        return result.data?.extras?.getString(PAYMENT_RESPONSE_EXTRA)
    }

    override fun getErrorCode(result: ActivityResult): String? {
        return result.data?.extras?.getString(PAYMENT_ERROR_EXTRA)
    }

    override fun parseResponse(
        result: ActivityResult,
        response: String,
        onSuccess: (result: PaymentResult) -> Unit,
        onFailure: (code: String) -> Unit
    ) {
        try {
            val model = Gson().fromJson(response, PaymentResult::class.java)

            if (isResponseSuccess(model)) {
                onSuccess.invoke(model)
            } else {
                val error = getErrorCode(result) ?: PAYMENT_DEFAULT_ERROR
                onFailure.invoke(error)
            }
        } catch (e: JsonSyntaxException) {
            onFailure.invoke(PAYMENT_DEFAULT_ERROR)
        }
    }

    private fun isResponseSuccess(response: PaymentResult): Boolean {
        return response.status == PAYMENT_RESPONSE_APPROVED_STATUS
    }

    companion object {
        private const val PAYMENT_RESPONSE_APPROVED_STATUS = "approved"
        private const val PAYMENT_RESPONSE_ERROR_STATUS = "error"

        private const val PAYMENT_RESPONSE_EXTRA = "PAYMENT_RESPONSE"
        private const val PAYMENT_ERROR_EXTRA = "PAYMENT_ERROR"

        private const val PAYMENT_DEFAULT_ERROR = "UNKNOWN_ERROR"
    }
}
