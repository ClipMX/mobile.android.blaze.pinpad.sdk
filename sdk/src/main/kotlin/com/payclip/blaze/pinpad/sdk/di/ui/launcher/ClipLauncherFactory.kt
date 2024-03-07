package com.payclip.blaze.pinpad.sdk.di.ui.launcher

import com.payclip.blaze.pinpad.sdk.di.ui.activity.ClipResultManagerFactory
import com.payclip.blaze.pinpad.sdk.di.ui.intent.ClipIntentProviderFactory
import com.payclip.blaze.pinpad.sdk.ui.launcher.ClipLauncher
import com.payclip.blaze.pinpad.sdk.ui.launcher.activity.ActivityClipLauncher

object ClipLauncherFactory {

    fun create(): ClipLauncher {
        val resultManager = ClipResultManagerFactory.create()
        val intentProvider = ClipIntentProviderFactory.create()

        return ActivityClipLauncher(resultManager, intentProvider)
    }
}
