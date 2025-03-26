package org.trevor.pcup.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.trevor.pcup.CenteringColumn
import org.trevor.pcup.Platform
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.today

class LimitsTab(private val platform: Platform) : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.today)
            return remember {
                TabOptions(
                    index = 0u,
                    "Limits",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() = Limits(platform)
}

@Composable
fun Limits(platform: Platform) {
    CenteringColumn {
        Text("App Limits")
        platform.sendNotification("Go work!", "Do your homework, its been 1 hour.");
    }
}
