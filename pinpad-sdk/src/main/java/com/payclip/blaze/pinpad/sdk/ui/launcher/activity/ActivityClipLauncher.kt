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
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher

internal class ActivityClipLauncher constructor(
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
        autoReturn: Boolean,
        preferences: PaymentPreferences
    ) {
        val launcher = getLauncher()
        val intent = intentProvider.getClipIntent(
            reference = reference,
            amount = amount,
            autoReturn = autoReturn,
            preferences = preferences
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
