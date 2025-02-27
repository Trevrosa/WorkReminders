package org.trevor.pcup.backend.auth

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.trevor.pcup.backend.BASE_URL

suspend fun authenticate(client: HttpClient, request: AuthRequest): AuthResult? {
    val origTag = Logger.tag
    Logger.setTag("authenticate")

    val response = client.post("${BASE_URL}/auth") {
        setBody(Json.encodeToString(request))
    }

    val result: AuthResult? = if (response.status == HttpStatusCode.OK) {
        Logger.d("got auth resp")

        val resp: AuthResult? = try {
            Logger.d("serializing from resp json")
            Json.decodeFromString(response.bodyAsText())
        } catch (e: Exception) {
            Logger.e("got error: $e")
            null
        }

        Logger.d("serialized auth resp")
        resp
    } else {
        Logger.e("post auth failed with ${response.status} status.")
        null
    }

    Logger.setTag(origTag)
    return result
}