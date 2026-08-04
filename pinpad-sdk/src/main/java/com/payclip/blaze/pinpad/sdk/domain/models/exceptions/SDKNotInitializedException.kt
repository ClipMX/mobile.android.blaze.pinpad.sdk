package com.payclip.blaze.pinpad.sdk.domain.models.exceptions

internal class SDKNotInitializedException :
    Exception("ClipPaymentSDK.initialize(...) must be called before starting a payment.") {

    companion object {
        const val ERROR_CODE = "SDK_NOT_INITIALIZED"
    }
}
