package org.trevor.pcup

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DesktopApp() {
    val platform = getPlatform()

    var one by remember { mutableStateOf(true) }
    var two by remember { mutableStateOf(false) }
    var three by remember { mutableStateOf(false) }

    val go1 = { one = true; two = false; three = false }
    val go2 = { one = false; two = true; three = false }
    val go3 = { one = false; two = false; three = true }

    NavBar(go1, go2, go3)

    if (one) {
        CenteringColumn {
            Text(platform.name)
            Text(platform.batteryString())
        }
    }

    if (two) {
        CenteringColumn {
            Text("xdd")
        }
    }

    if (three) {
        CenteringColumn {
            Text("ppd")
        }
    }
}
