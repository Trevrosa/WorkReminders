package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

// FIXME: change when needed
// 10.0.2.2 is host localhost for android emulator
internal const val BASE_URL = "http://10.0.2.2:8000"

@Suppress("NAME_SHADOWING")
inline fun <reified T> handleResult(response: JsonObject?): T? {
    val response = response ?: return null

    // handle the Ok and Err variants separately.
    return response["Ok"]?.let { e ->
        // its Ok! we can get the T straight from it.
        Json.decodeFromJsonElement(e)
    } ?: response["Err"]?.let { e ->
        // its an Err... lets say so.
        Logger.e("got err: $e", tag = "handleResponse")
        null
    }
}

inline fun <reified T> encodeOption(value: T?): String {
    return if (value == null) {
        // the none variant
        Json.decodeFromString("null")
    } else {
        // Some variant
        Json.encodeToString(value)
    }
}

suspend fun handleResponse(response: HttpResponse?): JsonObject? {
    return if (response == null) {
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
}
