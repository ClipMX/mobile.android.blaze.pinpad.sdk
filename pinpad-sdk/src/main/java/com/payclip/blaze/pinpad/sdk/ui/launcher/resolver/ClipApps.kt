package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.ComponentName
import com.payclip.blaze.pinpad.sdk.BuildConfig

internal object ClipApps {
    const val PINPAD_BASE_PACKAGE = "com.payclip.blaze.pinpad"
    const val SMARTPOS_BASE_PACKAGE = "com.payclip.blaze.smartpos"
    const val BLAZE_BASE_PACKAGE = "com.payclip.clip"

    val PINPAD_PACKAGE = PINPAD_BASE_PACKAGE + BuildConfig.CLIP_APP_SUFFIX
    val SMARTPOS_PACKAGE = SMARTPOS_BASE_PACKAGE + BuildConfig.CLIP_APP_SUFFIX
    val BLAZE_PACKAGE = BLAZE_BASE_PACKAGE + BuildConfig.BLAZE_APP_SUFFIX

    // La entry activity se queda con el namespace de PinPad aunque el applicationId lleve sufijo.
    val PINPAD_ENTRY = ComponentName(PINPAD_PACKAGE, "$PINPAD_BASE_PACKAGE.shared.ui.MainActivity")

    // El orden es el del selector cuando hay más de una instalada.
    val ALLOWED_PACKAGES = listOf(PINPAD_PACKAGE, SMARTPOS_PACKAGE, BLAZE_PACKAGE)
}
