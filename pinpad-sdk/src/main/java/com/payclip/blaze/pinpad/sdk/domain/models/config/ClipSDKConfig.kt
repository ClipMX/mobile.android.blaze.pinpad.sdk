package com.payclip.blaze.pinpad.sdk.domain.models.config

import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment

internal data class ClipSDKConfig(
    /** Full `Authorization` header value: `Basic base64(apiKey:secretKey)`. */
    val authHeader: String,
    val environment: ClipEnvironment
)
