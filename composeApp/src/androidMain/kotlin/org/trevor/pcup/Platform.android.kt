package org.trevor.pcup

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger

class AndroidPlatform(private val ctx: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val battery: Int?
        get() {
            val battery = ctx.getSystemService(BATTERY_SERVICE) as? BatteryManager
            if (battery == null) {
                Logger.i("could not get BATTERY_SERVICE")
                return null
            }
            return battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }

    init {
        Logger.setTag("APP")
        Logger.i("AndroidPlatform init")
        Logger.i("ctx: $ctx")
    }
}

@Composable
actual fun getPlatform(): Platform = AndroidPlatform(LocalContext.current)
