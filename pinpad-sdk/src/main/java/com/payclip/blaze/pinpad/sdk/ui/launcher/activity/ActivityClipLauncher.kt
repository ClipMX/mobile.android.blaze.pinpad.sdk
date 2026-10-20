package com.payclip.blaze.pinpad.sdk.ui.launcher.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.payclip.blaze.pinpad.sdk.domain.listener.login.LoginListener
import com.payclip.blaze.pinpad.sdk.domain.listener.payment.PaymentListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApplicationNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.LoginListenerInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.login.LoginResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipAppChoiceStore
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipAppChooser
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipAppQuery
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipAppResolution
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipAppResolver
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ClipApps
import com.payclip.blaze.pinpad.sdk.ui.launcher.resolver.ResolvedBy

internal class ActivityClipLauncher(
    private val resultManager: ClipResultManager,
    private val intentProvider: ClipIntentProvider
) : ClipLauncher {

    private lateinit var aLauncher: ActivityResultLauncher<Intent>

    private lateinit var cLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>

    private var hostContext: Context? = null

    private var paymentListener: PaymentListener? = null

    private val resolver = ClipAppResolver(ClipApps.ALLOWED_PACKAGES, ClipApps.PINPAD_ENTRY)

    override fun setPaymentHandler(
        activity: ComponentActivity,
        paymentListener: PaymentListener,
        loginListener: LoginListener?
    ) {
        hostContext = activity
        this.paymentListener = paymentListener
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
        hostContext = LocalContext.current
        this.paymentListener = paymentListener
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
                    onFailure = { code: String, failedResult: PaymentResult? ->
                        paymentListener.onFailure(code, failedResult)
                    }
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
        requestPaymentPreferences: RequestPaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin?,
        webhookUrl: String?
    ) {
        val launcher = getLauncher()
        val intent = intentProvider.getClipIntent(
            reference = reference,
            amount = amount,
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            requestPaymentPreferences = requestPaymentPreferences,
            clipLoginCredentials = clipLoginCredentials,
            webhookUrl = webhookUrl
        )

        launchResolved(launcher, intent)
    }

    override fun startPayment(
        reference: String,
        amount: Double,
        tipAmount: Double,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        requestPaymentPreferences: RequestPaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin?,
        webhookUrl: String?
    ) {
        val launcher = getLauncher()
        val intent = intentProvider.getClipIntent(
            reference = reference,
            amount = amount,
            tipAmount = tipAmount,
            isAutoReturnEnabled = isAutoReturnEnabled,
            isRetryEnabled = isRetryEnabled,
            isShareEnabled = isShareEnabled,
            requestPaymentPreferences = requestPaymentPreferences,
            clipLoginCredentials = clipLoginCredentials,
            webhookUrl = webhookUrl
        )

        launchResolved(launcher, intent)
    }

    // El intent ya trae los extras; aquí solo se elige a qué app va. Siempre explícito: lleva credenciales.
    private fun launchResolved(launcher: ActivityResultLauncher<Intent>, intent: Intent) {
        val context = hostContext
        if (context == null) {
            launchSafely(launcher, intent)
            return
        }

        val store = ClipAppChoiceStore(context)
        val resolution = resolver.resolve(ClipAppQuery(context).findCandidates(), store.get())

        when (resolution) {
            is ClipAppResolution.Launch -> launchCandidate(launcher, intent, resolution)

            is ClipAppResolution.Choose -> {
                val activity = context.findActivity()
                if (activity == null) {
                    launchCandidate(launcher, intent, ClipAppResolution.Launch(resolution.candidates.first(), ResolvedBy.ACTION))
                } else {
                    ClipAppChooser.show(
                        activity = activity,
                        candidates = resolution.candidates,
                        onChosen = { chosen, remember ->
                            if (remember) store.save(chosen.packageName)
                            launchCandidate(launcher, intent, ClipAppResolution.Launch(chosen, ResolvedBy.ACTION))
                        },
                        onCancelled = { paymentListener?.onCancelled() }
                    )
                }
            }
        }
    }

    private fun launchCandidate(
        launcher: ActivityResultLauncher<Intent>,
        intent: Intent,
        resolution: ClipAppResolution.Launch
    ) {
        Log.i(TAG, "resolved_by=${resolution.resolvedBy.value} package=${resolution.candidate.packageName}")
        launchSafely(launcher, intent.setComponent(resolution.candidate.component))
    }

    private fun launchSafely(launcher: ActivityResultLauncher<Intent>, intent: Intent) {
        try {
            launcher.launch(intent)
        } catch (e: Exception) {
            throw ApplicationNotFoundException()
        }
    }

    private fun Context.findActivity(): Activity? {
        var current: Context? = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
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

    private companion object {
        const val TAG = "PinpadSdk"
    }
}
