package org.trevor.pcup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource
import org.trevor.pcup.CenteringColumn
import org.trevor.pcup.DataStore
import org.trevor.pcup.Platform
import org.trevor.pcup.SESSION_ID_KEY
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
