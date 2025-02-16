package org.trevor.pcup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import platform.UIKit.UIDevice

// @Composable unused here, but required because of the `expect`ed function
@Composable
actual fun getPlatform(): Platform = IOSPlatform()

class IOSPlatform : Platform {
    override val name: String =
        "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"
    override val battery: Int?
        get() {
            UIDevice.currentDevice.batteryMonitoringEnabled = true
            val battery = UIDevice.currentDevice.batteryLevel
            return if (battery == -1F) {
                Logger.i("battery monitoring not enabled")
                null
            } else {
                (battery * 100).toInt()
            }
        }

    override fun getScreenTimes(): List<ScreenTime>? {
        TODO("Not yet implemented")
    }

    init {
        Logger.i("init")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Row(Modifier.fillMaxWidth(0.9F), horizontalArrangement = Arrangement.Center) {
        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                columnSeries { series(data) }
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

