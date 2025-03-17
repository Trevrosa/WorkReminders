package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.trevor.pcup.Either

@Serializable
data class AuthRequest(val username: String, val password: String)

@Serializable
data class UserSession(@SerialName("user_id") val userId: UInt, val id: String)

/**
 * Either create a new account with name [AuthRequest.username], or sign in with password [AuthRequest.password].
 *
 * @return The session for the requested user.
 * @param client The HTTP client to make requests with.
 * @param request The requested username and password.
 */
suspend fun authenticate(
    client: HttpClient,
    request: AuthRequest
): Either<UserSession, JsonElement>? {
    val origTag = Logger.tag
    Logger.setTag("authenticate")

    Logger.d("sending request")
    val response = client.tryJsonPost("$BASE_URL/auth", Json.encodeToString(request))
    val result: JsonObject? = handleResponse(response)

    Logger.setTag(origTag)
    return handleResult(result)
}

suspend fun validateSession(client: HttpClient, sessionId: String): Boolean {
    val origTag = Logger.tag
    Logger.setTag("validateSession")

    Logger.d("sending request")
    val response = client.tryGet("$BASE_URL/auth/validate_session/$sessionId")
    val result = response?.bodyAsText() == "true"

    Logger.setTag(origTag)
    return result
}
