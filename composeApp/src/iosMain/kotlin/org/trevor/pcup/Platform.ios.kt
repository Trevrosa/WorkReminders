package org.trevor.pcup

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val battery: Float
        get() {
            UIDevice.currentDevice.batteryMonitoringEnabled = true
            return UIDevice.currentDevice.batteryLevel
        }

    init {
        Logger.i("init")
    }
}

// @Composable unused here, but required because of the expect ed function
@Composable
actual fun getPlatform(): Platform = IOSPlatform()
