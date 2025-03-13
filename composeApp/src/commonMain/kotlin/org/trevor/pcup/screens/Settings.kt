package org.trevor.pcup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import org.trevor.pcup.CenteringColumn
import org.trevor.pcup.Platform

@Composable
fun Settings(httpClient: HttpClient, platform: Platform) {
    CenteringColumn(arrangement = Arrangement.spacedBy(3.dp)) {
        Text("Settings")
        Logger.setTag("Settings")

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
