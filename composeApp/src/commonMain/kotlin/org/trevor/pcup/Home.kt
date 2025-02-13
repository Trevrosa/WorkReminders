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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.home
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class OptionInt {
    Some(),
}

// TODO: make the chart platform-independent (create new enum class for vico and a desktop chart one)
@Composable
fun ChartPart(apps: Collection<App>) {
    Row(Modifier.fillMaxWidth(0.9F), horizontalArrangement = Arrangement.Center) {
        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                columnSeries { series(apps.map { a -> a.time.inWholeMinutes.toInt() }) }
                Logger.d("created column series")
            }
        }

        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(guideline = null, tick = null, label = null),
                bottomAxis = HorizontalAxis.rememberBottom(
                    guideline = null,
                    tick = null,
                    label = null
                ),
            ), modelProducer
        )
        Logger.i("chart host init")
    }
}

class App(val name: String, val time: Duration, val icon: ImageVector)

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
        Modifier.rounded(1.dp).border(1.dp, Color.Gray)
            .fillMaxWidth(0.8F)
            .height(35.dp)
    ) {
        Row(
            Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val random = remember { Random(Clock.System.now().toEpochMilliseconds()) }
        val icon = vectorResource(Res.drawable.home)
        val apps = remember {
            List<App>(5) {
                App(
                    "app${it + 1}",
                    random.nextInt(1, 120).minutes,
                    icon
                )
            }
        }

        ChartPart(apps)
        HorizontalDivider(thickness = 2.dp)
        ListPart(apps)
    }
}
