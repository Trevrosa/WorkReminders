package org.trevor.pcup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.datetime.Clock
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIDevice
import platform.UIKit.UIViewController
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.posix.exit

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
     * There is no way to restart the app on ios.
     *
     * This function instead alerts the user that we will be exiting, and tells them to reopen it.
     */
    @Composable
    override fun restart() {
        val alert = UIAlertController.alertControllerWithTitle(
            "exiting",
            "i am exiting! please reopen the app.",
            UIAlertControllerStyleAlert
        )
        val alertAction = UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault) {
            Logger.d("alert ${it?.description()} pressed OK")
            exit(-1)
        }
        alert.addAction(alertAction)

        UIViewController().presentViewController(alert, true, null)
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

    /**
     * If `null`, then notification permissions have not been requested.
     * If not `null`, the value is whether we have notification permissions.
     */
    private var notificationPermission: Boolean? = null

    /**
     * Request permission to send notifications.
     *
     * @param center The notification center.
     */
    private fun requestNotificationPermission(center: UNUserNotificationCenter) {
        if (notificationPermission != null) {
            Logger.d("notification already requested: $notificationPermission")
            return
        }

        val authHandler: (Boolean, NSError?) -> Unit = { granted, err ->
            if (err != null) {
                Logger.e("got error $err")
            }
            notificationPermission = granted
            Logger.i("got notification permission: $granted")
        }
        center.requestAuthorizationWithOptions(options = UNAuthorizationOptionAlert, authHandler)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun dataStorePath(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path + "/$dataStoreName"
    }

    @Composable
    override fun sendNotification(title: String, message: String) {
        val origTag = Logger.tag
        Logger.setTag("sendNotification")

        val center = UNUserNotificationCenter.currentNotificationCenter()
        this.requestNotificationPermission(center)

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
        Logger.setTag(origTag)
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Canvas(Modifier.fillMaxSize()) {
        drawLine(Color.Blue, Offset(10F, 10F), Offset(10F, 12F), 10f)
    }
    Logger.e("Not yet implemented", tag = "Graph")
}
