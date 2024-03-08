package com.payclip.blaze.pinpad.sdk.ui.intent

import android.content.Intent

interface ClipIntentProvider {

    fun getClipIntent(
        requestId: String,
        autoReturn: Boolean = false
    ): Intent

    fun getRequestId(intent: Intent): String

    fun getAutoReturn(intent: Intent): Boolean
}
