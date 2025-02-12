package org.trevor.pcup

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.app_name

fun main() = application {
    var appName: String
    runBlocking {
        appName = getString(Res.string.app_name)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = appName,
    ) {
        App()
    }
}