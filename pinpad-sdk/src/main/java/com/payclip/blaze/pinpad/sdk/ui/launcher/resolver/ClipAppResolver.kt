package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.ComponentName

/**
 * Decide qué app de Clip cobra a partir de las candidatas que declaran CLIP_PAYMENT.
 * Con 0 candidatas cae al lanzamiento explícito a PinPad de siempre.
 */
internal class ClipAppResolver(
    private val allowedPackages: List<String>,
    private val fallback: ComponentName
) {

    fun resolve(found: List<ClipAppCandidate>, rememberedPackage: String?): ClipAppResolution {
        val candidates = found
            .filter { it.packageName in allowedPackages }
            .distinctBy { it.packageName }
            .sortedBy { allowedPackages.indexOf(it.packageName) }

        return when {
            candidates.isEmpty() -> ClipAppResolution.Launch(
                candidate = ClipAppCandidate(fallback, fallback.packageName),
                resolvedBy = ResolvedBy.FALLBACK
            )

            candidates.size == 1 -> ClipAppResolution.Launch(candidates.single(), ResolvedBy.ACTION)

            else -> candidates.firstOrNull { it.packageName == rememberedPackage }
                ?.let { ClipAppResolution.Launch(it, ResolvedBy.ACTION) }
                ?: ClipAppResolution.Choose(candidates)
        }
    }
}
