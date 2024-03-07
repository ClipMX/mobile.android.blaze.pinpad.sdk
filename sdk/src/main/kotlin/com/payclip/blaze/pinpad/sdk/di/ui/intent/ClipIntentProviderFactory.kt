package com.payclip.blaze.pinpad.sdk.di.ui.intent

import com.payclip.blaze.pinpad.sdk.ui.intent.ClipIntentProvider
import com.payclip.blaze.pinpad.sdk.ui.intent.system.SystemClipIntentProvider

object ClipIntentProviderFactory {

    fun create(): ClipIntentProvider {
        return SystemClipIntentProvider()
    }
}
