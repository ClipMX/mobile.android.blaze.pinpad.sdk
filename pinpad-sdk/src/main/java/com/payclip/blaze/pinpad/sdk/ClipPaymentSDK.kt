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
     * Initialize the SDK with your merchant API credentials.
     *
     * Calling this method again overrides the previous configuration, which allows credential
     * rotation at runtime.
     *
     * @param apiKey The API key of your merchant account.
     * @param secretKey The secret key paired with the API key.
     * @param environment The Clip backend environment to use. Defaults to [ClipEnvironment.PRODUCTION].
     *
     * @throws IllegalArgumentException if [apiKey] or [secretKey] are blank.
     */
    @JvmStatic
    @JvmOverloads
    fun initialize(
        apiKey: String,
        secretKey: String,
        environment: ClipEnvironment = ClipEnvironment.PRODUCTION
    ) {
        require(apiKey.isNotBlank()) { "apiKey must not be blank." }
        require(secretKey.isNotBlank()) { "secretKey must not be blank." }

        config = ClipSDKConfig(
            apiKey = apiKey,
            secretKey = secretKey,
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
}
