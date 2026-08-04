package com.payclip.blaze.pinpad.sdk.domain.models.environment

/**
 * Available Clip backend environments to point the SDK to.
 */
enum class ClipEnvironment(internal val baseUrl: String) {

    /**
     * Production environment. Use it for real payments.
     */
    PRODUCTION("https://api.payclip.io/"),

    /**
     * Stage environment. Use it for integration testing with test credentials.
     */
    STAGE("https://stage-api.payclip.io/")
}
