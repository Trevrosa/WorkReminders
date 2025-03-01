package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Serializable
data class AuthRequest(val username: String, val password: String)

@Serializable
data class UserSession(@SerialName("user_id") val userId: UInt, val id: String)

suspend fun authenticate(client: HttpClient, request: AuthRequest): UserSession? {
    val origTag = Logger.tag
    Logger.setTag("authenticate")

    Logger.d("sending request")
    val response = try {
        client.post("${BASE_URL}/auth") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }
    } catch (e: Exception) {
        Logger.e("got err: $e")
        null
    }

    val result: JsonObject? = if (response == null) {
        Logger.e("got null response")
        null
    } else if (response.status == HttpStatusCode.OK) {
        Logger.d("got ok response")

        val resp: JsonObject? = try {
            Logger.d("deserializing resp to json")
            val decoded: JsonObject = Json.decodeFromString(response.bodyAsText())
            Logger.d("deserialized successfully")

            decoded
        } catch (e: Exception) {
            Logger.e("got ser error: $e")
            null
        }

        resp
    } else {
        Logger.e("POST failed with ${response.status}")
        null
    }

    Logger.setTag(origTag)
    return handleResult(result)
}