package com.payclip.blaze.pinpad.sdk.domain.models.config

import com.payclip.blaze.pinpad.sdk.domain.models.environment.ClipEnvironment

internal data class ClipSDKConfig(
    val apiKey: String,
    val secretKey: String,
    val environment: ClipEnvironment
)
