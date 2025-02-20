package org.trevor.pcup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import co.touchlab.kermit.Logger
import platform.UIKit.UIDevice

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
//        platform.ScreenTime.STScreenTimeConfiguration
        Logger.e("Not yet implemented", tag = "getScreenTimeData")
        return null
    }

    @Composable
    override fun sendNotification(message: String) {
        Logger.e("Not yet implemented", tag = "sendNotification")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Logger.e("Not yet implemented", tag = "Graph")
}
