package io.github.partialize.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class AuthorizeException(message: String? = null, cause: Throwable? = null) : ResponseStatusException(HttpStatus.UNAUTHORIZED, message, cause)
