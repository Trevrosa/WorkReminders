package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.trevor.pcup.Either

@Serializable
data class SyncSummary(val data: UserData, val failed: UInt)

@Serializable
data class UserData(@SerialName("app_usage") val appUsage: List<AppInfo>)

@Serializable
data class AppInfo(val name: String, val usage: UInt, val limit: UInt)

// TODO: test this
/**
 * Synchronize local user state with the server.
 *
 * @return The synchronized user data state, attached with the number of sync failures, or null if any fatal errors occurred.
 *
 * @param client The HTTP client to use to make the request.
 * @param currentData The current user data, or null.
 * @param sessionId The user's session.
 */
suspend fun syncUserData(
    client: HttpClient,
    currentData: UserData?,
    sessionId: String
): Either<SyncSummary, JsonElement>? {
    val origTag = Logger.tag
    Logger.setTag("syncUserData")

    Logger.d("sending request")
    val response = client.tryJsonPost("${BASE_URL}/sync/$sessionId", encodeOption(currentData))
    val result: JsonObject? = handleResponse(response)

    Logger.setTag(origTag)
    return handleResult(result)
}