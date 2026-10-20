package com.payclip.blaze.pinpad.sdk.ui.launcher.resolver

import android.content.Context

internal class ClipAppChoiceStore(context: Context) {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun get(): String? = prefs.getString(KEY_PACKAGE, null)

    fun save(packageName: String) {
        prefs.edit().putString(KEY_PACKAGE, packageName).apply()
    }

    private companion object {
        const val PREFS_NAME = "pinpad_sdk_prefs"
        const val KEY_PACKAGE = "preferred_clip_app"
    }
}
