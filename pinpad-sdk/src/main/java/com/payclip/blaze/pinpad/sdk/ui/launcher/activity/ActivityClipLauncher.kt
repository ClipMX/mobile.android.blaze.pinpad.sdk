package com.payclip.blaze.pinpad.sdk.ui.launcher.activity

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.ApplicationNotFoundException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.PaymentInitializationException
import com.payclip.blaze.pinpad.sdk.domain.models.payment.PaymentResult
import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher

internal class ActivityClipLauncher constructor(
    private val resultManager: ClipResultManager,
    private val intentProvider: ClipIntentProvider
) : ClipLauncher {

    private lateinit var launcher: ManagedActivityResultLauncher<Intent, ActivityResult>

    @Composable
    override fun setPaymentHandler(
        onSuccess: (result: PaymentResult) -> Unit,
        onCancelled: () -> Unit,
        onFailure: (code: String) -> Unit
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val response = resultManager.getResponse(it)

                if (response != null) {
                    resultManager.parseResponse(
                        result = it,
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

        return launcher
    }

    override fun startPayment(
        requestId: String,
        autoReturn: Boolean,
        isTipEnabled: Boolean?
    ) {
        if (!::launcher.isInitialized) {
            throw PaymentInitializationException()
        }

        val intent = intentProvider.getClipIntent(
            requestId = requestId,
            autoReturn = autoReturn,
            isTipEnabled = isTipEnabled
        )

        try {
            launcher.launch(intent)
        } catch (e: Exception) {
            throw ApplicationNotFoundException()
        }
    }
}
