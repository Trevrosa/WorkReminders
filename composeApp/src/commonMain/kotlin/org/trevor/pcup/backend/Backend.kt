package org.trevor.pcup.backend

import co.touchlab.kermit.Logger
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
