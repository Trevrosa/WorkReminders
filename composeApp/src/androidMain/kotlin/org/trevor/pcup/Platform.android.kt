package org.trevor.pcup

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

class AndroidPlatform : Platform, Application() {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val battery: Float?
        get() {
            val batteryStatus = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
                this.baseContext.registerReceiver(null, filter)
            }
            return batteryStatus?.let { intent ->
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                level * 100 / scale.toFloat()
            }
        }
}

actual fun getPlatform(): Platform = AndroidPlatform()