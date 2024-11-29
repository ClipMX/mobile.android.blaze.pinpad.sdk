package com.payclip.blaze.pinpad.sdk.domain.listener.login

interface LoginListener {

    fun onLoginSuccess(result: String)

    fun onLoginFailure(code: String, message: String? = null)

}