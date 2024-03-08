package com.payclip.blaze.pinpad.sdk.di.ui.activity

import com.payclip.blaze.pinpad.sdk.ui.activity.ClipResultManager
import com.payclip.blaze.pinpad.sdk.ui.activity.result.ActivityClipResultManager

object ClipResultManagerFactory {

    fun create(): ClipResultManager {
        return ActivityClipResultManager()
    }
}
