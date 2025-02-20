package org.trevor.pcup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.trevor.pcup.Either.Left
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.home
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ChartPart(apps: Collection<App>) {
    Row(Modifier.fillMaxWidth().padding(2.dp, 0.dp)) {
        Graph(apps.map { it.time.inWholeSeconds })
    }
}

class App(val name: String, val time: Duration, val icon: ComposeImage)

@Composable
fun ListPart(apps: Collection<App>) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (app in apps) {
            AppRowItem(app)
        }
    }
}

@Composable
@Preview
fun AppRowItem(app: App) {
    Box(
        Modifier.rounded(5.dp).border(1.dp, Color.Gray)
            .fillMaxWidth(0.75F)
            .height(45.dp)
    ) {
        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            // TODO: maybe 2.5
            Spacer(Modifier.width(2.dp))
            Image(
                app.icon, "${app.name} icon",
                modifier = Modifier.fillMaxHeight(0.8F).rounded(1.dp)
            )
            VerticalDivider()
            Text(app.name)
            VerticalDivider()
            Text(app.time.toString())
        }
    }
}

@Composable
fun Home() {
    Column {
        val random = remember { Random(Clock.System.now().toEpochMilliseconds()) }
        val icon = vectorResource(Res.drawable.home)
        val apps = remember {
            List(5) {
                App(
                    "app${it + 1}",
                    random.nextInt(1, 120).minutes,
                    Left(icon)
                )
            }
        }
        val dbg = getPlatform().getScreenTimes()
        Logger.d(dbg?.toString() ?: "dbg was null")

        ChartPart(apps)
        HorizontalDivider(thickness = 2.dp)
        Spacer(Modifier.height(8.dp))
        ListPart(apps)
    }
}
