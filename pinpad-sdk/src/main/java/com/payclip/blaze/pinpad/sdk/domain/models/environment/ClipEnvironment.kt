package com.payclip.blaze.pinpad.sdk.domain.models.environment

/**
 * Available Clip backend environments to point the SDK to.
 */
enum class ClipEnvironment(
    internal val baseUrl: String,
    internal val defaultPinpadPackage: String
) {

    /**
     * Production environment. Use it for real payments. Payments open the release PinPad
     * application.
     */
    PRODUCTION("https://api.payclip.io/", "com.payclip.blaze.pinpad"),

    /**
     * Stage environment. Use it for integration testing with test credentials. Payments open
     * the QA PinPad application by default.
     */
    STAGE("https://stage-api.payclip.io/", "com.payclip.blaze.pinpad.qa")
}
