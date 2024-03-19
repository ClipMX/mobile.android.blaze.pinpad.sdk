package com.payclip.blaze.pinpad.sdk.ui.intent.system

import android.content.ComponentName
import android.content.Intent
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyAmountException
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyReferenceException
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider

internal class SystemClipIntentProvider : ClipIntentProvider {

    override fun getClipIntent(
        reference: String,
        amount: Double,
        autoReturn: Boolean,
        isTipEnabled: Boolean?
    ): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(PINPAD_PACKAGE, PINPAD_ENTRY_ACTIVITY)
            putExtra(PAYMENT_REFERENCE_EXTRA, reference)
            putExtra(PAYMENT_AMOUNT_EXTRA, amount.toString())
            putExtra(PAYMENT_AUTO_RETURN_EXTRA, autoReturn)
            putExtra(PAYMENT_IS_TIP_ENABLED_EXTRA, isTipEnabled)
        }
    }

    override fun getReference(intent: Intent): String {
        return intent.extras?.getString(PAYMENT_REFERENCE_EXTRA) ?: throw EmptyReferenceException()
    }

    override fun getAmount(intent: Intent): String {
        return intent.extras?.getString(PAYMENT_AMOUNT_EXTRA) ?: throw EmptyAmountException()
    }

    override fun getAutoReturn(intent: Intent): Boolean? {
        return intent.extras?.getBoolean(PAYMENT_AUTO_RETURN_EXTRA)
    }

    override fun isTipEnabled(intent: Intent): Boolean? {
        return intent.extras?.getBoolean(PAYMENT_IS_TIP_ENABLED_EXTRA)
    }

    companion object {
        private const val PINPAD_PACKAGE = "com.payclip.blaze.pinpad"
        private const val PINPAD_ENTRY_ACTIVITY = "$PINPAD_PACKAGE.views.MainActivity"

        private const val PAYMENT_REFERENCE_EXTRA = "PAYMENT_REFERENCE"
        private const val PAYMENT_AMOUNT_EXTRA = "PAYMENT_AMOUNT"
        private const val PAYMENT_AUTO_RETURN_EXTRA = "PAYMENT_AUTO_RETURN"
        private const val PAYMENT_IS_TIP_ENABLED_EXTRA = "PAYMENT_IS_TIP_ENABLED"
    }
}
