package org.trevor.pcup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.skribi
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ColumnScope.ChartPart() {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(5, 2, 3, 1) }
            Logger.d("created column series")
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ), modelProducer
    )
    Logger.i("chart host init")
}

@Composable
fun ColumnScope.ListPart() {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val random = Random(Clock.System.now().toEpochMilliseconds())
        fun randomTime(): Duration {
            Logger.d("random int generated")
            return random.nextInt(0, 120).minutes
        }

        repeat(3) {
            AppRowItem("app$it", imageResource(Res.drawable.skribi), randomTime())
        }
    }
}

@Composable
@Preview
fun ColumnScope.AppRowItem(name: String, icon: ImageBitmap, time: Duration) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(1.dp)).border(1.dp, Color.Black).height(20.dp)
            .fillMaxWidth(0.7F)
    ) {
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                icon, "$name icon",
                modifier = Modifier.fillMaxHeight(0.8F).clip(RoundedCornerShape(1.dp))
            )
            VerticalDivider(thickness = 2.dp)
            Text(name)
            VerticalDivider(thickness = 2.dp)
            Text(time.toString())
        }
    }
}

@Composable
fun Home() {
    Column {
        ChartPart()
        HorizontalDivider(thickness = 2.dp)
        Spacer(Modifier.height(8.dp))
        ListPart()
    }
}
