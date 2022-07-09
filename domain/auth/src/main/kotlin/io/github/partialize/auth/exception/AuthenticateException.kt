package io.github.partialize.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class AuthenticateException(message: String? = null, cause: Throwable? = null) : ResponseStatusException(HttpStatus.FORBIDDEN, message, cause)
