package org.trevor.pcup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger

@Composable
fun Settings(platform: Platform) {
    CenteringColumn(arrangement = Arrangement.spacedBy(3.dp)) {
        Text("Settings")

        var input by rememberSaveable { mutableStateOf("") }
        val setInput = { s: String -> if (s.length <= 10) input = s }

        var clickedOld by remember { mutableStateOf(false) }
        var clicked by remember { mutableStateOf(false) }

        Text(input, Modifier.background(Color.LightGray).border(1.dp, color = Color.Blue))
        TextField(
            input,
            onValueChange = setInput,
            placeholder = { Text("input") },
            singleLine = true
        )
        Button(onClick = { clickedOld = clicked; clicked = !clicked }) { Text("xdd") }

        // need this because is @Composable
        if (clicked != clickedOld) {
            platform.sendNotification("xdd", input)
        }

        val dbg = platform.getScreenTimeData()
        
        Button(onClick = {
            if (dbg != null) {
                Logger.d("screen times: $dbg")
                Logger.d("first: ${dbg.firstOrNull()?.appName}: ${dbg.firstOrNull()?.usage}")
            } else {
                Logger.e("screen time was null")
            }
        }) { Text("screen") }
    }
}
