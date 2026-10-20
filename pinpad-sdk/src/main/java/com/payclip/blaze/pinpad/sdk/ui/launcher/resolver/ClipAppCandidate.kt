package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.ComponentName
import android.graphics.drawable.Drawable

internal data class ClipAppCandidate(
    val component: ComponentName,
    val label: String,
    val icon: Drawable? = null
) {
    val packageName: String get() = component.packageName
}

internal enum class ResolvedBy(val value: String) {
    ACTION("action"),
    FALLBACK("fallback")
}

internal sealed interface ClipAppResolution {
    data class Launch(val candidate: ClipAppCandidate, val resolvedBy: ResolvedBy) : ClipAppResolution
    data class Choose(val candidates: List<ClipAppCandidate>) : ClipAppResolution
}
