package com.payclip.blaze.pinpad.sdk.domain.models.login

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoginResult(
    val code: String = "",
    val detail: String = ""
): Parcelable
