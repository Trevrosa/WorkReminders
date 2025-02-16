package org.trevor.pcup

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.os.BatteryManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import kotlin.time.Duration.Companion.milliseconds

@Composable
actual fun getPlatform(): Platform = AndroidPlatform(LocalContext.current)

class AndroidPlatform(private val ctx: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val battery: Int?
        get() {
            val battery = ctx.getSystemService(BATTERY_SERVICE) as? BatteryManager
            if (battery == null) {
                Logger.e("could not get BATTERY_SERVICE")
                return null
            }
            return battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }

    override fun getScreenTimes(): List<ScreenTime>? {
        val usageStats = ctx.getSystemService(USAGE_STATS_SERVICE) as? UsageStatsManager
        if (usageStats == null) {
            Logger.e("could not get USAGE_STATS_SERVICE")
            return null
        }

        val stats = usageStats.queryAndAggregateUsageStats(0, System.currentTimeMillis())
        Logger.d("got ${stats.size} usage stats")
        return stats.map { (k, v) -> ScreenTime(k, v.totalTimeInForeground.milliseconds) }
    }

    init {
        Logger.setTag("APP")
        Logger.i("AndroidPlatform init")
        Logger.i("ctx: $ctx")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
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
            startAxis = VerticalAxis.rememberStart(
                guideline = null,
                tick = null,
                label = null,
                line = null
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                tick = null,
                label = null,
                line = null,
            ),
        ), modelProducer
    )
    Logger.i("chart host init")

}
