package org.trevor.pcup.backend.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.trevor.pcup.backend.DBErrorKind

@Serializable
sealed class AuthResult {
    class Ok(val session: UserSession)
    class Err(val error: AuthError)
}

@Serializable
sealed class AuthError {
    class InvalidPassword(val kind: InvalidPasswordKind)
    class WrongPassword
    class HashError(val kind: HashErrorKind)
    class DBError(val kind: DBErrorKind)
    class InternalError
}

@Serializable
enum class InvalidPasswordKind {
    NotEnoughChars,
    TooManyChars,
}

@Serializable
enum class HashErrorKind {
    CreateError,
    ParseError,
}

@Serializable
data class AuthRequest(val username: String, val password: String)

@Serializable
data class User(val name: String, val id: UInt)

@Serializable
data class UserSession(@SerialName("user_id") val userId: UInt, val id: String)
