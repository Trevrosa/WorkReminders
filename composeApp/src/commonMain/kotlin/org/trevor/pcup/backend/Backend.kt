package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import org.trevor.pcup.Either

// FIXME: change as needed
// 10.0.2.2 is host localhost for android emulator
internal const val BASE_URL = "https://pcup.trevrosa.dev"

suspend fun HttpClient.tryGet(url: String): HttpResponse? {
    return try {
        this.get(url)
    } catch (e: Exception) {
        Logger.e("got err: $e")
        null
    }
}

suspend fun HttpClient.tryJsonPost(url: String, jsonBody: String = ""): HttpResponse? {
    return try {
        this.post(url) {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }
    } catch (e: Exception) {
        Logger.e("got err: $e")
        null
    }
}

/**
 * Deserialize `serde_json`-style `Result`s to a [T] or the error.
 *
 * @return The deserialized type or the error as a [JsonElement], represented as an [Either].
 * @param obj The nullable [JsonObject] to be deserialized.
 */
@Suppress("NAME_SHADOWING")
inline fun <reified T> handleResult(obj: JsonObject?): Either<T, JsonElement>? {
    val obj = obj ?: return null

    // handle the Ok and Err variants separately.
    return obj["Ok"]?.let { e ->
        // its Ok! we can get the T straight from it.
        Either.Left(Json.decodeFromJsonElement(e))
    } ?: obj["Err"]?.let { e ->
        // its an Err... lets say so.
        Logger.e("got err: $e", tag = "handleResponse")
        Either.Right(e)
    }
}

/**
 * Deserialize the given [value] as a nullable type to a [String].
 *
 * @return The deserialized [String].
 * @param value The nullable value.
 */
inline fun <reified T> encodeOption(value: T?): String {
    return if (value == null) {
        // the none variant
        Json.decodeFromString("\"null\"")
    } else {
        // Some variant
        Json.encodeToString(value)
    }
}

/**
 * Deserialize the [response] to a [JsonObject], or `null`.
 *
 * @return The resulting Json Object.
 * @param response The HTTP response to handle.
 */
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
