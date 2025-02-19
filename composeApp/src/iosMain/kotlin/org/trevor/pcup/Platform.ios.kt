package org.trevor.pcup

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
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
//        platform.ScreenTime.STScreenTimeConfiguration
        Logger.e("Not yet implemented")
        return null
    }

    init {
        Logger.i("init")
    }
}

@Composable
actual fun Graph(data: Collection<Number>) {
    Logger.e("Not yet implemented")
}

