package com.payclip.blaze.pinpad.sdk.ui.launcher.activity

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApplicationNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.login.LoginResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher

internal class ActivityClipLauncher(
    private val resultManager: ClipResultManager,
    private val intentProvider: ClipIntentProvider
) : ClipLauncher {

    private lateinit var aLauncher: ActivityResultLauncher<Intent>

    private lateinit var cLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    override fun setPaymentHandler(
        activity: ComponentActivity,
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    ) {
        aLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            onHandleResult(
                result = it,
                onSuccess = onSuccess,
                onCancelled = onCancelled,
                onFailure = onFailure
            )
        }
    }

    @Composable
    override fun setPaymentHandler(
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    ) {
        cLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            onHandleResult(
                result = it,
                onSuccess = onSuccess,
                onCancelled = onCancelled,
                onFailure = onFailure
            )
        }
    }

    override fun setLoginHandler(
        activity: ComponentActivity,
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit,
    ) {
        aLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            onHandleLoginResult(
                result = it,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    @Composable
    override fun setLoginHandler(
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit
    ) {
        cLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ){
            onHandleLoginResult(
                result = it,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    private fun onHandleLoginResult(
        result: ActivityResult,
        onSuccess: (LoginResult) -> Unit,
        onFailure: (LoginResult) -> Unit,
    ){
        if (result.resultCode == Activity.RESULT_OK) {
            val response = resultManager.getLoginResponse(result)

            if(response != null){
                resultManager.parseLoginResponse(
                    result = result,
                    response = response,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            } else {
                resultManager.getLoginExceptionResponse(
                    LOGIN_ACTIVITY_RESULT_EXCEPTION_CODE,
                    LOGIN_ACTIVITY_RESULT_EXCEPTION_MESSAGE
                )?.let { onFailure.invoke(it) }
            }
        }else {
            resultManager.getLoginExceptionResponse(
                LOGIN_ACTIVITY_RESPONSE_EXCEPTION_CODE,
                LOGIN_ACTIVITY_RESPONSE_EXCEPTION_MESSAGE
            )?.let { onFailure.invoke(it) }
        }
    }

    private fun onHandleResult(
        result: ActivityResult,
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    ) {
        if (result.resultCode == Activity.RESULT_OK) {
            val response = resultManager.getResponse(result)

            if (response != null) {
                resultManager.parseResponse(
                    result = result,
                    response = response,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            } else {
                onCancelled.invoke()
            }
        } else {
            onCancelled.invoke()
        }
    }

    override fun startPayment(
        reference: String,
        amount: Double,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        preferences: PaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin?
    ) {
        val launcher = getLauncher()
        val intent = intentProvider.getClipIntent(
            reference = reference,
            amount = amount,
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            preferences = preferences,
            clipLoginCredentials = clipLoginCredentials
        )

        try {
            launcher.launch(intent)
        } catch (e: Exception) {
            throw ApplicationNotFoundException()
        }
    }

    private fun getLauncher(): ActivityResultLauncher<Intent> {
        if (::aLauncher.isInitialized) {
            return aLauncher
        }

        if (::cLauncher.isInitialized) {
            return cLauncher
        }

        throw PaymentInitializationException()
    }

    companion object {
        private const val LOGIN_ACTIVITY_RESULT_EXCEPTION_CODE = "ACTIVITY_RESULT_FAILURE"
        private const val LOGIN_ACTIVITY_RESULT_EXCEPTION_MESSAGE = "The activity result has not ok response"
        private const val LOGIN_ACTIVITY_RESPONSE_EXCEPTION_CODE = "ACTIVITY_RESPONSE_FAILURE"
        private const val LOGIN_ACTIVITY_RESPONSE_EXCEPTION_MESSAGE = "The activity response has not login response object"
    }
}
