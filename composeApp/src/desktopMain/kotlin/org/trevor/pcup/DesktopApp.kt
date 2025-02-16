package org.trevor.pcup

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DesktopApp() {
    val platform = getPlatform()
    CenteringColumn {
        Text(platform.name)
        Text(platform.batteryString())
    }
}