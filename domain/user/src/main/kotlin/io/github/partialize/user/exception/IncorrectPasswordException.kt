package io.github.partialize.user.exception

import io.github.partialize.auth.exception.AuthorizeException

class IncorrectPasswordException(message: String? = null) : AuthorizeException(message)
