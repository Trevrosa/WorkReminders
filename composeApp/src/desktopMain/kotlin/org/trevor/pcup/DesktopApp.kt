package org.trevor.pcup

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun DesktopApp() {
    val platform = getPlatform()
    Text(platform.name)
    Text(platform.batteryString())
}