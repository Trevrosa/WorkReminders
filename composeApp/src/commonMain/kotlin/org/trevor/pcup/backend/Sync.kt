package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
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
    val response = post(client, "${BASE_URL}/sync/$sessionId", encodeOption(currentData))
    val result: JsonObject? = handleResponse(response)

    Logger.setTag(origTag)
    return handleResult(result)
}