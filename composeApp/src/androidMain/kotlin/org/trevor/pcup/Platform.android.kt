package org.trevor.pcup

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context.USAGE_STATS_SERVICE
import android.graphics.drawable.Icon
import android.os.BatteryManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
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
import workreminders.composeapp.generated.resources.Res
import workreminders.composeapp.generated.resources.skribi
import kotlin.time.Duration.Companion.milliseconds

const val CHANNEL_ID = "defaultChannel"

@SuppressLint("ComposableNaming")
@Composable
@NonRestartableComposable
actual fun init() {
    val ctx = LocalContext.current

    Logger.setTag("PlatformInit")
    Logger.i("xdd")
    Logger.i("ctx: $ctx")
    Logger.d("the xdd service is ${ctx.getSystemService("xdd")}")

    val channel = NotificationChannel(
        CHANNEL_ID,
        "Work Reminders",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    channel.description = "notifications xdd"

    val notificationManager =
        ctx.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
    notificationManager?.createNotificationChannel(channel)
        ?: Logger.e("no notification manager.")

    Logger.i("created notification channel with id $CHANNEL_ID")
}

@Composable
actual fun getPlatform(): Platform = AndroidPlatform(LocalContext.current)

class AndroidPlatform(private val ctx: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val battery: Int?
        get() {
            val battery = ctx.getSystemService(BATTERY_SERVICE) as? BatteryManager
            if (battery == null) {
                Logger.e("could not get BATTERY_SERVICE", tag = "Platform::battery")
                return null
            }
            return battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }

    // TODO: find out why this doesn't work
    override fun getScreenTimeData(): List<ScreenTime>? {
        val origTag = Logger.tag
        Logger.setTag("getScreenTimeData")

        val usageStats = ctx.getSystemService(USAGE_STATS_SERVICE) as? UsageStatsManager
        if (usageStats == null) {
            Logger.e("could not get USAGE_STATS_SERVICE")
            return null
        }
        val stats =
            usageStats.queryAndAggregateUsageStats(0, Clock.System.now().toEpochMilliseconds())
        Logger.d("got ${stats.size} usage stats")

        Logger.setTag(origTag)
        return stats.map { (k, v) -> ScreenTime(k, v.totalTimeInForeground.milliseconds) }
    }

    override fun dataStorePath(): String = ctx.filesDir.resolve(dataStoreName).absolutePath

    @Composable
    // TODO: allow caller to specify notification icon
    override fun sendNotification(title: String, message: String) {
        val origTag = Logger.tag
        Logger.setTag("sendNotification")

        val notificationManager = ctx.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
        if (notificationManager == null) {
            Logger.w("NOTIFICATION_SERVICE was null")
            return
        }

        val notification = Notification.Builder(ctx, CHANNEL_ID)
            .setColor(0x38c761)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(Icon.createWithBitmap(imageResource(Res.drawable.skribi).asAndroidBitmap()))
            .build()

        val id = Clock.System.now().toEpochMilliseconds().toInt()
        notificationManager.notify(id, notification)

        Logger.i("sent notification with id $id")
        Logger.setTag(origTag)
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(data) }
            Logger.d("created column series", tag = "Graph::LaunchedEffect")
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
    Logger.d("chart host created", tag = "Graph")
}
