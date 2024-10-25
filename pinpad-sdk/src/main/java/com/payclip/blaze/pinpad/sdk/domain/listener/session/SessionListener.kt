package com.payclip.blaze.pinpad.sdk.domain.listener.session

interface SessionListener {

    fun onSuccess(session: String)

    fun onFailure(code: String)

}