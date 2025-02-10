package org.trevor.pcup

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val battery: Float = UIDevice.currentDevice.batteryLevel
}

actual fun getPlatform(): Platform = IOSPlatform()