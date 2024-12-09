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
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApplicationNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.LoginListenerInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentInitializationException
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
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    ) {
        aLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            onHandleResult(
                result = it,
                paymentListener, loginListener
            )
        }
    }

    @Composable
    override fun setPaymentHandler(
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    ) {
        cLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            onHandleResult(
                result = it,
                paymentListener, loginListener
            )
        }
    }

    private fun onHandleLoginResult(
        activityResult: ActivityResult,
        loginListener: LoginListener?
    ) {
        val responseSuccess = resultManager.getLoginResponse(activityResult)
        val responseError = resultManager.getLoginErrorResponse(activityResult)

        val response = responseSuccess ?: responseError.orEmpty()

        if (loginListener != null) {
            resultManager.parseLoginResponse(
                result = activityResult,
                response = response,
                onSuccess = { loginResult: LoginResult -> loginListener.onLoginSuccess(loginResult.code) },
                onFailure = { loginResult: LoginResult ->
                    loginListener.onLoginFailure(
                        loginResult.code,
                        loginResult.detail
                    )
                }
            )
        } else {
            throw LoginListenerInitializationException()
        }
    }

    private fun onHandleResult(
        result: ActivityResult,
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    ) {
        if (result.resultCode == Activity.RESULT_OK) {
            val responsePayment = resultManager.getResponse(result)

            if (responsePayment != null) {
                resultManager.parseResponse(
                    result = result,
                    response = responsePayment,
                    onSuccess = paymentListener::onSuccess,
                    onFailure = paymentListener::onFailure
                )
            } else {
                onHandleLoginResult(result, loginListener)
            }
        } else {
            paymentListener.onCancelled()
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

}
