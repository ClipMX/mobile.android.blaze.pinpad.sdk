package com.payclip.blaze.pinpad.sdk.ui.activity.result

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.payclip.blaze.pinpad.sdk.domain.models.login.LoginResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager

class ActivityClipResultManager : ClipResultManager {

    override fun setSuccessResult(
        activity: Activity,
        reference: String,
        amount: String,
        receiptNumber: String
    ) {
        val result = PaymentResult(
            reference = reference,
            status = PAYMENT_RESPONSE_APPROVED_STATUS,
            amount = amount,
            receiptNumber = receiptNumber
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

    override fun setLoginSuccessResult(activity: Activity, code: String, successMessage: String) {
        val successResult = loginResultStructure(code, successMessage)

        activity.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(LOGIN_RESPONSE_SUCCESS_EXTRA, Gson().toJson(successResult))
            }
        )
    }

    override fun setLoginErrorResult(activity: Activity, code: String, errorMessage: String) {
        val errorResult = loginResultStructure(code, errorMessage)

        activity.setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(LOGIN_RESPONSE_ERROR_EXTRA, Gson().toJson(errorResult))
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

    override fun getLoginResponse(result: ActivityResult): String? {
        return result.data?.extras?.getString(LOGIN_RESPONSE_SUCCESS_EXTRA)
    }

    override fun getLoginExceptionResponse(code: String, exceptionMessage: String): LoginResult {
        return loginResultStructure(
            code = code,
            message = exceptionMessage
        )
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

    override fun parseLoginResponse(
        result: ActivityResult,
        response: String,
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit
    ) {
        try {
            val loginModel = Gson().fromJson(response, LoginResult::class.java)

            if(isLoginResponseSuccess(loginModel)){
                onSuccess.invoke(loginModel)
            }else{
                onFailure.invoke(loginModel)
            }
        }catch (e: JsonSyntaxException){
            val loginModelException = e.message?.let {
                LoginResult(
                    code = LOGIN_DEFAULT_ERROR,
                    detail = it
                )
            } ?: LoginResult()
            onFailure.invoke(loginModelException)
        }
    }

    private fun isLoginResponseSuccess(response: LoginResult): Boolean {
        return response.code == LOGIN_RESPONSE_SUCCESS_CODE
    }

    private fun isResponseSuccess(response: PaymentResult): Boolean {
        return response.status == PAYMENT_RESPONSE_APPROVED_STATUS
    }

    private fun loginResultStructure(code: String, message: String): LoginResult {
        return LoginResult(
            code = code,
            detail = message
        )
    }

    companion object {
        private const val PAYMENT_RESPONSE_APPROVED_STATUS = "approved"
        private const val PAYMENT_RESPONSE_ERROR_STATUS = "error"

        private const val PAYMENT_RESPONSE_EXTRA = "PAYMENT_RESPONSE"
        private const val PAYMENT_ERROR_EXTRA = "PAYMENT_ERROR"

        private const val PAYMENT_DEFAULT_ERROR = "UNKNOWN_ERROR"

        private const val LOGIN_DEFAULT_ERROR = "LOGIN_UNKNOWN_ERROR"
        private const val LOGIN_RESPONSE_SUCCESS_CODE = "LOGIN_SUCCESS"

        private const val LOGIN_RESPONSE_SUCCESS_EXTRA = "LOGIN_SUCCESS_EXTRA"
        private const val LOGIN_RESPONSE_ERROR_EXTRA = "LOGIN_ERROR_EXTRA"
    }
}
