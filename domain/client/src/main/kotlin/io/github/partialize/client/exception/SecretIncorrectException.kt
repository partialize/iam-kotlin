package io.github.partialize.client.exception

import io.github.partialize.auth.exception.AuthorizeException

class SecretIncorrectException(message: String? = null) : AuthorizeException(message)
