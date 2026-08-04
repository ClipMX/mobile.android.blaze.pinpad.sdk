package com.payclip.blaze.pinpad.sdk

import com.payclip.blaze.pinpad.sdk.domain.models.config.ClipSDKConfig
import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment
import java.util.UUID

/**
 * Global configuration entry point of the PinPad SDK.
 *
 * Call [initialize] once (e.g. in your `Application.onCreate`) with the API credentials of your
 * merchant account, obtained from the Clip developers portal. Once initialized, every payment
 * started with `ClipPayment.start(...)` will first create a payment request against the Clip
 * backend and attach its id to the intent sent to the PinPad application, so the payment can be
 * validated as belonging to your integration.
 *
 * If the SDK is not initialized the payment flow keeps working as before, without the payment
 * request step.
 */
object ClipPaymentSDK {

    @Volatile
    private var config: ClipSDKConfig? = null

    /**
     * Serial number identifier sent to the backend as `serial_number_pos`. It is randomly
     * generated once per process and prefixed with `sdk-` so backend can tell SDK-created
     * requests apart from physical POS serial numbers.
     */
    internal val serialNumber: String by lazy { "$SERIAL_PREFIX${UUID.randomUUID()}" }

    /**
     * Initialize the SDK with your merchant API token.
     *
     * The token is the authentication string generated in the Clip Developers Portal: the
     * Base64 encoding of `apiKey:secretKey`. It can be provided with or without the `Basic `
     * prefix, the SDK normalizes it.
     *
     * Calling this method again overrides the previous configuration, which allows credential
     * rotation at runtime.
     *
     * @param apiToken The API token of your merchant account (`Basic xxx` or just `xxx`).
     * @param environment The Clip backend environment to use. Defaults to [ClipEnvironment.PRODUCTION].
     *
     * @throws IllegalArgumentException if [apiToken] is blank.
     */
    @JvmStatic
    @JvmOverloads
    fun initialize(
        apiToken: String,
        environment: ClipEnvironment = ClipEnvironment.PRODUCTION
    ) {
        require(apiToken.isNotBlank()) { "apiToken must not be blank." }

        val normalizedToken = apiToken.trim().removePrefix(BASIC_PREFIX).trim()

        config = ClipSDKConfig(
            authHeader = "$BASIC_PREFIX$normalizedToken",
            environment = environment
        )
    }

    /**
     * @return true if [initialize] was already called with valid credentials.
     */
    @JvmStatic
    fun isInitialized(): Boolean = config != null

    internal fun getConfigOrNull(): ClipSDKConfig? = config

    private const val SERIAL_PREFIX = "sdk-"
    private const val BASIC_PREFIX = "Basic "
}
