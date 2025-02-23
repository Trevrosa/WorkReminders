package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import platform.Foundation.NSError
import platform.UIKit.UIDevice
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

// @Composable unused here, but required because of the `expect`ed function
@Composable
actual fun getPlatform(): Platform = IOSPlatform()

@Composable
@NonRestartableComposable
actual fun init() {
    Logger.i("xdd", tag = "PlatformInit")
}

class IOSPlatform : Platform {
    override val name: String =
        "${UIDevice.currentDevice.systemName()} ${UIDevice.currentDevice.systemVersion}"
    override val battery: Int?
        get() {
            UIDevice.currentDevice.batteryMonitoringEnabled = true
            val battery = UIDevice.currentDevice.batteryLevel
            return if (battery == -1F) {
                Logger.w("battery monitoring not enabled", tag = "Platform::battery")
                null
            } else {
                (battery * 100).toInt()
            }
        }

    override fun getScreenTimeData(): List<ScreenTime>? {
//        platform.
//        platform.ScreenTime.STScreenTimeConfiguration
        Logger.e("Not yet implemented", tag = "getScreenTimeData")
        return null
    }

    /**
     * Log [NSError]s
     */
    private val errHandler: (String, NSError?) -> Unit = { context, err ->
        if (err != null) {
            Logger.e(
                "got error code ${err.code}: $err",
                tag = context
            )
        }
    }

    @Composable
    override fun sendNotification(title: String, message: String) {
        Logger.setTag("sendNotification")

        val center = UNUserNotificationCenter.currentNotificationCenter()

        var notificationPermission: Boolean? = null

        val authHandler: (Boolean, NSError?) -> Unit = { granted, err ->
            if (err != null) {
                Logger.e("got error $err")
            }
            notificationPermission = granted
            Logger.i("got notification permission? $notificationPermission")
        }
        center.requestAuthorizationWithOptions(options = UNAuthorizationOptionAlert, authHandler)

        while (notificationPermission == null) {
            runBlocking { delay(1000) }
            Logger.d("waited 1s for permission")
        }

        if (!notificationPermission!!) {
            Logger.e("no send notification permission.")
            return
        }

        val content = UNMutableNotificationContent()
        content.setTitle(title)
        content.setBody(message)
        Logger.d("created NotificationContent")

        val id = Clock.System.now().toEpochMilliseconds().toString()
        // trigger = null means send it immediately
        val request = UNNotificationRequest.requestWithIdentifier(id, content, null)
        Logger.d("created NotificationRequest")

        // send it now!
        center.addNotificationRequest(request) { err -> errHandler("sendNotification", err) }
        Logger.i("sent notification with id $id")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Logger.e("Not yet implemented", tag = "Graph")
}
