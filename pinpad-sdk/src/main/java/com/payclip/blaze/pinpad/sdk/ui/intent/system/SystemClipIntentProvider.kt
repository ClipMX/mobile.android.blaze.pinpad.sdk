package com.payclip.blaze.pinpad.sdk.ui.intent.system

import android.content.ComponentName
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import com.google.gson.Gson
import com.payclip.blaze.pinpad.sdk.domain.models.login.ClipPaymentLogin
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.PaymentPreferences
import com.payclip.blaze.pinpad.sdk.domain.models.payment.settings.RequestPaymentPreferences
import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider

internal class SystemClipIntentProvider : ClipIntentProvider {

    override fun getClipIntent(
        reference: String,
        amount: Double,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        requestPaymentPreferences: RequestPaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin?
    ): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(PINPAD_PACKAGE, PINPAD_ENTRY_ACTIVITY)
            putExtra(PAYMENT_REFERENCE_EXTRA, reference)
            putExtra(PAYMENT_AMOUNT_EXTRA, amount.toString())
            putExtra(PAYMENT_AUTO_RETURN_EXTRA, isAutoReturnEnabled)
            putExtra(PAYMENT_RETRY_EXTRA, isRetryEnabled)
            putExtra(PAYMENT_SHARE_EXTRA, isShareEnabled)
            putExtra(REQUEST_PAYMENT_PREFERENCES_EXTRA, Gson().toJson(requestPaymentPreferences))
            putExtra(CLIP_LOGIN_CREDENTIALS_EXTRA, clipLoginCredentials)
        }
    }

    override fun getClipIntent(
        reference: String,
        amount: Double,
        tipAmount: Double,
        isAutoReturnEnabled: Boolean,
        isRetryEnabled: Boolean,
        isShareEnabled: Boolean,
        requestPaymentPreferences: RequestPaymentPreferences,
        clipLoginCredentials: ClipPaymentLogin?
    ): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            component = ComponentName(PINPAD_PACKAGE, PINPAD_ENTRY_ACTIVITY)
            putExtra(PAYMENT_REFERENCE_EXTRA, reference)
            putExtra(PAYMENT_AMOUNT_EXTRA, amount.toString())
            putExtra(PAYMENT_TIP_AMOUNT_EXTRA, tipAmount.toString())
            putExtra(PAYMENT_AUTO_RETURN_EXTRA, isAutoReturnEnabled)
            putExtra(PAYMENT_RETRY_EXTRA, isRetryEnabled)
            putExtra(PAYMENT_SHARE_EXTRA, isShareEnabled)
            putExtra(REQUEST_PAYMENT_PREFERENCES_EXTRA, Gson().toJson(requestPaymentPreferences))
            putExtra(CLIP_LOGIN_CREDENTIALS_EXTRA, clipLoginCredentials)
        }
    }

    override fun getReference(intent: Intent): String? {
        return intent.extras?.getString(PAYMENT_REFERENCE_EXTRA)
    }

    override fun getAmount(intent: Intent): String? {
        return intent.extras?.getString(PAYMENT_AMOUNT_EXTRA)
    }

    override fun getTipAmount(intent: Intent): String? {
        return intent.extras?.getString(PAYMENT_TIP_AMOUNT_EXTRA)
    }

    override fun isAutoReturnEnabled(intent: Intent): Boolean? {
        return intent.extras?.getBoolean(PAYMENT_AUTO_RETURN_EXTRA)
    }

    override fun isRetryEnabled(intent: Intent): Boolean? {
        return intent.extras?.getBoolean(PAYMENT_RETRY_EXTRA)
    }

    override fun isShareEnabled(intent: Intent): Boolean? {
        return intent.extras?.getBoolean(PAYMENT_SHARE_EXTRA)
    }

    @Deprecated(
        "Use getRequestPaymentPreferences instead. This method will be removed in a future version.",
        replaceWith = ReplaceWith("getRequestPaymentPreferences(intent)"),
        level = DeprecationLevel.WARNING
    )
    @Suppress("DEPRECATION")
    override fun getPaymentPreferences(intent: Intent): PaymentPreferences {
        val preferences = when {
            SDK_INT >= TIRAMISU -> intent.getParcelableExtra(
                PAYMENT_PREFERENCES_EXTRA,
                PaymentPreferences::class.java
            )

            else -> intent.extras?.getParcelable(PAYMENT_PREFERENCES_EXTRA)
        }
        return preferences ?: PaymentPreferences()
    }

    override fun getRequestPaymentPreferences(intent: Intent): String? {
        return intent.extras?.getString(REQUEST_PAYMENT_PREFERENCES_EXTRA)
    }

    override fun getPaymentLoginCredentials(intent: Intent): ClipPaymentLogin? {
        val loginCredentials: ClipPaymentLogin? = when {
            SDK_INT >= TIRAMISU -> intent.getParcelableExtra(
                CLIP_LOGIN_CREDENTIALS_EXTRA,
                ClipPaymentLogin::class.java
            )

            else -> intent.extras?.getParcelable(CLIP_LOGIN_CREDENTIALS_EXTRA)
        }

        return if (loginCredentials?.userAccount.isNullOrEmpty() || loginCredentials?.passwordAccount.isNullOrEmpty())
            return null else loginCredentials
    }

    companion object {
        private const val PINPAD_PACKAGE = "com.payclip.blaze.pinpad"
        private const val PINPAD_ENTRY_ACTIVITY = "$PINPAD_PACKAGE.shared.ui.MainActivity"

        private const val PAYMENT_REFERENCE_EXTRA = "PAYMENT_REFERENCE"
        private const val PAYMENT_AMOUNT_EXTRA = "PAYMENT_AMOUNT"
        private const val PAYMENT_TIP_AMOUNT_EXTRA = "PAYMENT_TIP_AMOUNT"
        private const val PAYMENT_SHARE_EXTRA = "PAYMENT_SHARE"
        private const val PAYMENT_RETRY_EXTRA = "PAYMENT_RETRY"
        private const val PAYMENT_AUTO_RETURN_EXTRA = "PAYMENT_AUTO_RETURN"
        private const val PAYMENT_PREFERENCES_EXTRA = "PAYMENT_PREFERENCES"
        private const val REQUEST_PAYMENT_PREFERENCES_EXTRA = "REQUEST_PAYMENT_PREFERENCES"
        private const val CLIP_LOGIN_CREDENTIALS_EXTRA = "CLIP_LOGIN_CREDENTIALS"
    }
}
