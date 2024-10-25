package com.payclip.blaze.pinpad.sdk.domain.usecases.session

import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyCredentialsException

internal class ClipLoginUseCase {
    operator fun invoke(
        email: String,
        password: String
    ): Result<Unit>{
        return try {
            if (email.isBlank() || password.isBlank()){
                throw EmptyCredentialsException()
            }
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}