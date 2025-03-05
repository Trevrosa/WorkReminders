package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class UserData(@SerialName("app_usage") val appUsage: List<AppInfo>)

@Serializable
data class AppInfo(val name: String, val usage: UInt, val limit: UInt)

// TODO: test this
/**
 * Synchronize local user state with the server.
 *
 * @return The synchronized user data state, or null if any errors occurred.
 *
 * @param client The HTTP client to use to make the request.
 * @param currentData The current user data, or null.
 * @param sessionId The user's session.
 */
suspend fun syncUserData(client: HttpClient, currentData: UserData?, sessionId: String): UserData? {
    val origTag = Logger.tag
    Logger.setTag("syncUserData")

    Logger.d("sending request")
    val response = try {
        client.post("${BASE_URL}/sync/$sessionId") {
            contentType(ContentType.Application.Json)
            setBody(encodeOption(currentData))
        }
    } catch (e: Exception) {
        Logger.e("got err: $e")
        null
    }

    val result: JsonObject? = handleResponse(response)

    Logger.setTag(origTag)
    return handleResult(result)
}