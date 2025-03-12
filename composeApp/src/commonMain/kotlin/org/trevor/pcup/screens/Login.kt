package org.trevor.pcup.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trevor.pcup.CenteringColumn

// FIXME: this needs to do auth through api, then store the session in datastore
@Composable
@Preview
fun Login() {
    CenteringColumn {
        Text("Log In", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        var username by remember { mutableStateOf("") }
        val setUsername = { s: String -> if (s.length <= 64) username = s }
        TextField(username, setUsername, placeholder = { Text("username") })

        Spacer(Modifier.height(10.dp))

        var password by remember { mutableStateOf("") }
        val setPassword = { s: String -> if (s.length <= 64) password = s }
        TextField(password, setPassword, placeholder = { Text("password") })

        Spacer(Modifier.height(20.dp))

        var clicked by remember { mutableStateOf(false) }
        var clickedOld by remember { mutableStateOf(false) }

        Button(onClick = { clickedOld = clicked; clicked = !clicked }) { Text("submit") }

        // need this because we need @Composable
        if (clicked != clickedOld) {
            val coroutineScope = rememberCoroutineScope()

        }
    }
}