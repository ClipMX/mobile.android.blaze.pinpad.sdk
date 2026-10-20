package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU

internal class ClipAppQuery(private val context: Context) {

    // Requiere el <queries> del manifest del SDK: sin él, en Android 11+ vuelve vacío y se cae al fallback sin error.
    fun findCandidates(): List<ClipAppCandidate> {
        val pm = context.packageManager
        val intent = Intent(CLIP_PAYMENT_ACTION).addCategory(Intent.CATEGORY_DEFAULT)
        return queryActivities(pm, intent).mapNotNull { info ->
            val activity = info.activityInfo ?: return@mapNotNull null
            ClipAppCandidate(
                component = android.content.ComponentName(activity.packageName, activity.name),
                label = info.loadLabel(pm).toString(),
                icon = info.loadIcon(pm)
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun queryActivities(pm: PackageManager, intent: Intent): List<ResolveInfo> = when {
        SDK_INT >= TIRAMISU -> pm.queryIntentActivities(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )

        else -> pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }

    companion object {
        const val CLIP_PAYMENT_ACTION = "com.payclip.action.CLIP_PAYMENT"
    }
}
