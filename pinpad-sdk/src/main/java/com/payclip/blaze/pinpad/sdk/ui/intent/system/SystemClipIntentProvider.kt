package com.payclip.blaze.pinpad.sdk.ui.intent.system

import android.content.ComponentName
import android.content.Intent
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider

internal class SystemClipIntentProvider : ClipIntentProvider {

    override fun getClipIntent(
        requestId: String,
        autoReturn: Boolean,
        isTipEnabled: Boolean
    ): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(PINPAD_PACKAGE, PINPAD_ENTRY_ACTIVITY)
            putExtra(PAYMENT_REQUEST_ID_EXTRA, requestId)
            putExtra(PAYMENT_AUTO_RETURN_EXTRA, autoReturn)
            putExtra(PAYMENT_IS_TIP_ENABLED_EXTRA, isTipEnabled)
        }
    }

    override fun getRequestId(intent: Intent): String {
        return intent.extras?.getString(PAYMENT_REQUEST_ID_EXTRA).orEmpty()
    }

    override fun getAutoReturn(intent: Intent): Boolean {
        return intent.extras?.getBoolean(PAYMENT_AUTO_RETURN_EXTRA) ?: false
    }

    override fun isTipEnabled(intent: Intent): Boolean {
        return intent.extras?.getBoolean(PAYMENT_IS_TIP_ENABLED_EXTRA) ?: false
    }

    companion object {
        private const val PINPAD_PACKAGE = "com.payclip.blaze.pinpad"
        private const val PINPAD_ENTRY_ACTIVITY = "$PINPAD_PACKAGE.views.MainActivity"

        private const val PAYMENT_AUTO_RETURN_EXTRA = "PAYMENT_AUTO_RETURN"
        private const val PAYMENT_REQUEST_ID_EXTRA = "PAYMENT_REQUEST_ID"
        private const val PAYMENT_IS_TIP_ENABLED_EXTRA = "PAYMENT_IS_TIP_ENABLED"
    }
}
