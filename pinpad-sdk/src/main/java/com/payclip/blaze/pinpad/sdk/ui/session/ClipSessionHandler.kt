package com.payclip.blaze.pinpad.sdk.ui.session

internal interface ClipSessionHandler {

    fun setSessionHandler(
        onSuccess: (session: String) -> Unit,
        onError: (code: String) -> Unit
    )
}