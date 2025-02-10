package org.trevor.pcup

import android.app.Application
import android.os.Build

class AndroidPlatform : Platform, Application() {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val battery: Float
        get() {
            return 2F
        }
}

actual fun getPlatform(): Platform = AndroidPlatform()