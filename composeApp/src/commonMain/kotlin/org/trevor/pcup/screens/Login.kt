package org.trevor.pcup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trevor.pcup.CenteringColumn
import org.trevor.pcup.DataStore
import org.trevor.pcup.Either
import org.trevor.pcup.SESSION_ID_KEY
import org.trevor.pcup.backend.AuthRequest
import org.trevor.pcup.backend.UserSession
import org.trevor.pcup.backend.authenticate

@Composable
@Preview
fun Login(httpClient: HttpClient): Boolean {
    Logger.setTag("Login")
    var ok by remember { mutableStateOf(false) }
    CenteringColumn {
        Text("Log In", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        var username by remember { mutableStateOf("") }
        val setUsername = { s: String -> username = s }
        TextField(username, setUsername, placeholder = { Text("username") })

        Spacer(Modifier.height(10.dp))

        var password by remember { mutableStateOf("") }
        val setPassword = { s: String -> if (s.length <= 64) password = s }
        TextField(password, setPassword, placeholder = { Text("password") })

        Spacer(Modifier.height(10.dp))

        // TODO: change this to status and give ok messages as well as error.
        var error by remember { mutableStateOf("") }
        if (error.isNotEmpty()) {
            Box(Modifier.background(Color.Red.copy(0.8f)).padding(1.dp)) {
                Text(error, color = Color.White)
            }
        }

        Spacer(Modifier.height(10.dp))

        var clicked by remember { mutableStateOf(false) }

        Button(onClick = { clicked = true }) { Text("submit") }

        // need this because we need @Composable
        if (clicked) {
            clicked = false;
            error = ""
            var session: Either<UserSession, JsonElement>? by remember { mutableStateOf(null) }

            runBlocking {
                Logger.i("authenticating on click")
                session = authenticate(httpClient, AuthRequest(username, password))
            }

            Logger.i("coroutine done")
            if (session != null) {
                val sessionOk = session?.left()
                val sessionErr = session?.right()

                if (sessionOk == null) {
                    error = try {
                        val e = Json.encodeToString(sessionErr!!)
                        Logger.i("set error message")
                        e
                    } catch (e: Exception) {
                        Logger.e("could not parse err json: $e")
                        "failed to parse err json"
                    }
                } else {
                    Logger.i("received session, ok!")
                    runBlocking {
                        DataStore.edit {
                            it[SESSION_ID_KEY] = sessionOk.id
                        }
                        Logger.d("set datastore session")
                    }
                    ok = true
                }
            } else {
                Logger.e("session result was null")
            }
        }
    }
    return ok
}
