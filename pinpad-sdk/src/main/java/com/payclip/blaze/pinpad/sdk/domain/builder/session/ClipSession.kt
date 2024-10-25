package com.payclip.blaze.pinpad.sdk.domain.builder.session

import com.payclip.blaze.pinpad.sdk.domain.listener.session.SessionListener
import com.payclip.blaze.pinpad.sdk.domain.models.exceptions.EmptyCredentialsException
import com.payclip.blaze.pinpad.sdk.domain.usecases.session.ClipLoginUseCase

/**
 * Represents a session for Clip login.
 *
 * This class handles the login process
 * It interacts with the `ClipLoginUseCase` to perform the login operation.
 *
 * @property useCase The use case responsible for handling the login logic.
 * @property sessionListener An optional listener to receive session updates.
 * @constructor Creates a new ClipSession instance.
 */
class ClipSession internal constructor(
    private val useCase: ClipLoginUseCase,
    private val sessionListener: SessionListener?
){
    /**
     * Builder class for creating ClipSession instances.
     *
     * This builder allows you to configure the email, password, and listener
     * before creating a ClipSession object.
     */
    class Builder {

        private var listener: SessionListener? = null

        fun addListener(listener: SessionListener) = apply {
            this.listener = listener
        }

        fun build(): ClipSession {
            val useCase = ClipLoginUseCase()
            return ClipSession(
                useCase = useCase,
                sessionListener = listener
            )
        }
    }

    fun setClipSessionListener(listener: SessionListener) {

    }

    fun login( email: String, password: String ) {
        useCase.invoke(
            email = email,
            password = password
        ).onSuccess {

        }.onFailure {
            val code = when(it){
                is EmptyCredentialsException -> EmptyCredentialsException.ERROR_CODE
                else -> DEFAULT_SESSION_ERROR
            }

            sessionListener?.onFailure(code)
        }
    }

    companion object {
        internal const val DEFAULT_SESSION_ERROR = "UNKNOWN_SESSION_ERROR"
    }

}