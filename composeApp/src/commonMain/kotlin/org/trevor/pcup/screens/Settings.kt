package org.trevor.pcup.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.trevor.pcup.CenteringColumn
import org.trevor.pcup.DataStore
import org.trevor.pcup.Platform
import org.trevor.pcup.SESSION_ID_KEY
import org.trevor.pcup.backend.UserData
import org.trevor.pcup.backend.UserDebug
import org.trevor.pcup.backend.syncUserData
import org.trevor.pcup.getSession
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.settings

data class SettingsTab(val httpClient: HttpClient, val platform: Platform) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.settings)
            return remember {
                TabOptions(
                    index = 0u,
                    "Settings",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() = Settings(httpClient, platform)
}

@Composable
fun Settings(httpClient: HttpClient, platform: Platform) {
    CenteringColumn(arrangement = Arrangement.spacedBy(3.dp)) {
        Text("Settings")
        Logger.setTag("Settings")

        var logout by remember { mutableStateOf(false) }
        Button(
            colors = ButtonDefaults.buttonColors(Color.Red),
            onClick = { logout = true }
        ) { Text("log out ;(", color = Color.White) }

        var synced: UserData? by rememberSaveable { mutableStateOf(null) }
        LaunchedEffect(Unit) {
            val resp = syncUserData(
                httpClient,
                UserData(appUsage = listOf(), debug = listOf()),
                getSession()!!
            )

            if (resp?.left() != null) {
                synced = resp.left()!!.data;
            }
        }

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (synced != null) {
                for ((idx, debug) in synced!!.debug.withIndex()) {
                    Box(Modifier.border(2.dp, Color.Gray)) {
                        Text("debug[$idx]: $debug")
                    }
                }
            } else {
                Text("loading sync data")
                Logger.e("synced was null")
            }
        }

        Spacer(Modifier.height(2.5.dp))

        var newItemToSync by remember { mutableStateOf("") }
        TextField(newItemToSync, onValueChange = { newItemToSync = it }, singleLine = true)

        Spacer(Modifier.height(2.5.dp))

        var summary by remember { mutableStateOf("") }
        Text(summary)

        var sync by remember { mutableStateOf(false) }
        Button(onClick = { sync = true }) { Text("sync") }

        val coroutine = rememberCoroutineScope()
        if (sync) {
            sync = false;
            Logger.i("syncing $newItemToSync")
            coroutine.launch {
                val toSync = if (newItemToSync.isEmpty()) {
                    UserData(appUsage = listOf(), debug = listOf())
                } else {
                    UserData(appUsage = listOf(), debug = listOf(UserDebug(newItemToSync)))
                };

                val resp = syncUserData(
                    httpClient,
                    toSync,
                    getSession()!!
                )
                if (resp != null) {
                    val respOk = resp.left()
                    val respErr = resp.right()

                    if (respOk != null) {
                        Logger.d(respOk.data.toString())
                        synced = respOk.data
                        summary = "ok, with ${respOk.failed} failed"
                    } else {
                        summary = "${respErr!!}"
                    }
                } else {
                    summary = "null"
                }
            }
        }

        if (logout) {
            logout = false
            Logger.i("user logged out, removing stored session")
            runBlocking {
                DataStore.edit {
                    it.remove(SESSION_ID_KEY)
                }
            }
            platform.restart()
        }
//        var input by rememberSaveable { mutableStateOf("") }
//        val setInput = { s: String -> if (s.length <= 10) input = s }
//
//        var clickedOld by remember { mutableStateOf(false) }
//        var clicked by remember { mutableStateOf(false) }
//
//        Text(input, Modifier.background(Color.LightGray).border(1.dp, color = Color.Blue))
//        TextField(
//            input,
//            onValueChange = setInput,
//            placeholder = { Text("input") },
//            singleLine = true
//        )
//        Button(onClick = { clickedOld = clicked; clicked = !clicked }) { Text("xdd") }
//
//        // need this because platform functions are @Composable
//        if (clicked != clickedOld) {
//            platform.sendNotification("xdd", input)
//
//            val dbg = platform.getScreenTimeData()
//            if (dbg != null) {
//                Logger.d("screen times: $dbg")
//                Logger.d("first: ${dbg.firstOrNull()?.appName}: ${dbg.firstOrNull()?.usage}")
//            } else {
//                Logger.e("screen time was null")
//            }
//        }

//        val coroutineScope = rememberCoroutineScope()
//        Button(onClick = {
//            coroutineScope.launch {
//                val session = authenticate(httpClient, AuthRequest("xdd", "12345678"))
//                Logger.i("authed session: $session")
//                if (session != null) {
//                    val exd = UserData(listOf(AppInfo("peepeepk1", 1u, 0u)))
//                    val userData = syncUserData(httpClient, exd, session.id)
//                    Logger.i("synced userdata: $userData")
//                }
//            }
//        }) { Text("http!") }
    }
}
